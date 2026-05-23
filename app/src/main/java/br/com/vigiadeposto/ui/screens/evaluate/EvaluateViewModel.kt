package br.com.vigiadeposto.ui.screens.evaluate

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vigiadeposto.data.repository.ResponsesRepository
import br.com.vigiadeposto.data.service.OpenStreetMapService
import br.com.vigiadeposto.domain.model.Station
import br.com.vigiadeposto.domain.model.Status
import br.com.vigiadeposto.domain.model.Vote
import br.com.vigiadeposto.domain.repository.FirebaseRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class EvaluateViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val responsesRepository: ResponsesRepository,
    private val openStreetMapService: OpenStreetMapService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(EvaluateUiState())
    val uiState: StateFlow<EvaluateUiState> = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        loadStations()
        loadQuickResponses()
    }

    private fun loadQuickResponses() {
        viewModelScope.launch {
            try {
                val (positivas, negativas, intermediarias) = responsesRepository.carregarRespostas()
                _uiState.value = _uiState.value.copy(
                    respostasPositivas = positivas,
                    respostasNegativas = negativas,
                    respostasIntermediarias = intermediarias
                )
            } catch (e: Exception) {
                // Usa respostas fallback que já estão no repositório
            }
        }
    }

    fun loadStations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let { loc ->
                    val currentLocation = LatLng(loc.latitude, loc.longitude)
                    
                    Log.d("EvaluateViewModel", "Carregando postos próximos a ${loc.latitude}, ${loc.longitude}")
                    
                    // 1. Busca postos do OpenStreetMap (postos reais e gratuitos)
                    val osmStations = openStreetMapService.findNearbyGasStations(
                        currentLocation,
                        5000 // 5km em metros
                    )
                    
                    Log.d("EvaluateViewModel", "OpenStreetMap encontrou ${osmStations.size} postos")
                    
                    // 2. Busca postos do Firebase (adicionados pelos usuários)
                    val firebaseStations = try {
                        firebaseRepository.getStationsNear(loc.latitude, loc.longitude, 20.0)
                    } catch (e: Exception) {
                        Log.w("EvaluateViewModel", "Erro ao buscar postos do Firebase", e)
                        emptyList()
                    }
                    
                    Log.d("EvaluateViewModel", "Firebase tem ${firebaseStations.size} postos")
                    
                    // 3. Combina as duas listas (OpenStreetMap + Firebase)
                    val allStations = (osmStations + firebaseStations).distinctBy { it.id }
                    
                    // Ordena por distância
                    val sortedStations = allStations.sortedBy { station ->
                        calculateDistance(
                            loc.latitude, loc.longitude,
                            station.lat ?: 0.0, station.lng ?: 0.0
                        )
                    }
                    
                    Log.d("EvaluateViewModel", "Total de ${sortedStations.size} postos únicos ordenados por distância")
                    
                    _uiState.value = _uiState.value.copy(
                        stations = sortedStations,
                        currentLocation = currentLocation,
                        isLoading = false
                    )
                } ?: run {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Não foi possível obter a localização",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                Log.e("EvaluateViewModel", "Erro ao carregar estações", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erro ao carregar estações: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun voteStation(stationId: String, voteValue: Int) {
        voteStationWithText(stationId, voteValue, "", "")
    }

    fun voteStationWithText(stationId: String, voteValue: Int, tipo: String, status: String) {
        viewModelScope.launch {
            // Marca como votando
            _uiState.value = _uiState.value.copy(
                votingStations = _uiState.value.votingStations + stationId
            )
            
            try {
                val result = firebaseRepository.voteStationWithText(stationId, voteValue, tipo, status)
                if (result is br.com.vigiadeposto.domain.model.Result.Success) {
                    // Atualiza a estação localmente
                    val updatedStations = _uiState.value.stations.map { station ->
                        if (station.id == stationId) {
                            when (voteValue) {
                                1 -> station.copy(pos = station.pos + 1)
                                -1 -> station.copy(neg = station.neg + 1)
                                else -> station
                            }
                        } else {
                            station
                        }
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        stations = updatedStations,
                        votingStations = _uiState.value.votingStations - stationId,
                        showVoteSuccess = true
                    )
                    
                    // Esconde o feedback após 2 segundos
                    kotlinx.coroutines.delay(2000)
                    _uiState.value = _uiState.value.copy(showVoteSuccess = false)
                } else {
                    _uiState.value = _uiState.value.copy(
                        votingStations = _uiState.value.votingStations - stationId,
                        errorMessage = "Erro ao votar: ${(result as br.com.vigiadeposto.domain.model.Result.Error).exception.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    votingStations = _uiState.value.votingStations - stationId,
                    errorMessage = "Erro ao votar: ${e.message}"
                )
            }
        }
    }

    fun setStatusFilter(status: Status?) {
        _uiState.value = _uiState.value.copy(statusFilter = status)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    private fun calculateDistance(
        lat1: Double, lng1: Double, lat2: Double, lng2: Double
    ): Double {
        val r = 6371.0 // Raio da Terra em km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }
}

data class EvaluateUiState(
    val stations: List<Station> = emptyList(),
    val currentLocation: LatLng? = null,
    val statusFilter: Status? = null,
    val votingStations: Set<String> = emptySet(),
    val showVoteSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val respostasPositivas: List<String> = emptyList(),
    val respostasNegativas: List<String> = emptyList(),
    val respostasIntermediarias: List<String> = emptyList()
) {
    val filteredStations: List<Station>
        get() = if (statusFilter != null) {
            stations.filter { it.status == statusFilter }
        } else {
            stations
        }
}
