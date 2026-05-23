package br.com.vigiadeposto.ui.screens.map

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vigiadeposto.data.service.OpenStreetMapService
import br.com.vigiadeposto.domain.model.Station
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
class MapViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    private val openStreetMapService: OpenStreetMapService,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        loadCurrentLocation()
    }

    fun loadCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val location = fusedLocationClient.lastLocation.await()
                location?.let { loc ->
                    val currentLocation = LatLng(loc.latitude, loc.longitude)
                    _uiState.value = _uiState.value.copy(
                        currentLocation = currentLocation,
                        isLoading = false
                    )
                    loadNearbyStations(loc.latitude, loc.longitude)
                } ?: run {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Não foi possível obter a localização",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erro ao obter localização: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadNearbyStations(lat: Double, lng: Double, radius: Double = 10.0) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingStations = true)
            
            try {
                Log.d("MapViewModel", "Buscando postos próximos a $lat, $lng")
                
                // 1. Busca postos do OpenStreetMap (postos reais e gratuitos)
                val osmStations = openStreetMapService.findNearbyGasStations(
                    LatLng(lat, lng),
                    (radius * 1000).toInt() // Converte km para metros
                )
                
                Log.d("MapViewModel", "OpenStreetMap encontrou ${osmStations.size} postos")
                
                // 2. Busca postos do Firebase (adicionados pelos usuários)
                val firebaseStations = try {
                    firebaseRepository.getStationsNear(lat, lng, radius)
                } catch (e: Exception) {
                    Log.w("MapViewModel", "Erro ao buscar postos do Firebase", e)
                    emptyList()
                }
                
                Log.d("MapViewModel", "Firebase tem ${firebaseStations.size} postos")
                
                // 3. Combina as duas listas (OpenStreetMap + Firebase)
                val allStations = (osmStations + firebaseStations).distinctBy { it.id }
                
                Log.d("MapViewModel", "Total de ${allStations.size} postos únicos")
                
                _uiState.value = _uiState.value.copy(
                    stations = allStations,
                    isLoadingStations = false
                )
                
            } catch (e: Exception) {
                Log.e("MapViewModel", "Erro ao carregar estações", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erro ao carregar estações: ${e.message}",
                    isLoadingStations = false
                )
            }
        }
    }

    fun searchStations(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)
            
            try {
                val allStations = firebaseRepository.getAllStations()
                val filteredStations = allStations.filter { station ->
                    station.name?.contains(query, ignoreCase = true) == true ||
                    station.address?.contains(query, ignoreCase = true) == true
                }
                
                _uiState.value = _uiState.value.copy(
                    searchResults = filteredStations,
                    isSearching = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erro na pesquisa: ${e.message}",
                    isSearching = false
                )
            }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchResults = emptyList(),
            searchQuery = ""
        )
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun onStationSelected(station: Station) {
        _uiState.value = _uiState.value.copy(selectedStation = station)
    }

    fun clearSelectedStation() {
        _uiState.value = _uiState.value.copy(selectedStation = null)
    }
}

data class MapUiState(
    val currentLocation: LatLng? = null,
    val stations: List<Station> = emptyList(),
    val searchResults: List<Station> = emptyList(),
    val selectedStation: Station? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isLoadingStations: Boolean = false,
    val isSearching: Boolean = false,
    val errorMessage: String? = null
)
