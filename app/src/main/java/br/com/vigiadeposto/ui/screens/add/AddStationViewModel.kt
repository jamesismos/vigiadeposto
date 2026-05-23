package br.com.vigiadeposto.ui.screens.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vigiadeposto.domain.model.Station
import br.com.vigiadeposto.domain.repository.FirebaseRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AddStationViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddStationUiState())
    val uiState: StateFlow<AddStationUiState> = _uiState.asStateFlow()

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
                    // Preenche endereço automaticamente
                    fillAddressFromLocation(loc.latitude, loc.longitude)
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

    private suspend fun fillAddressFromLocation(lat: Double, lng: Double) {
        try {
            // Aqui você pode implementar reverse geocoding
            // Por enquanto, vamos usar coordenadas como endereço
            val address = "Lat: ${String.format("%.6f", lat)}, Lng: ${String.format("%.6f", lng)}"
            _uiState.value = _uiState.value.copy(address = address)
        } catch (e: Exception) {
            // Se falhar, mantém o endereço vazio
        }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateAddress(address: String) {
        _uiState.value = _uiState.value.copy(address = address)
    }

    fun updateCnpj(cnpj: String) {
        _uiState.value = _uiState.value.copy(cnpj = cnpj)
    }

    fun addStation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            
            try {
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser == null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Usuário não autenticado",
                        isSubmitting = false
                    )
                    return@launch
                }

                val currentLocation = _uiState.value.currentLocation
                if (currentLocation == null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Localização não disponível",
                        isSubmitting = false
                    )
                    return@launch
                }

                val station = Station(
                    name = _uiState.value.name.takeIf { it.isNotEmpty() },
                    address = _uiState.value.address.takeIf { it.isNotEmpty() },
                    location = GeoPoint(currentLocation.latitude, currentLocation.longitude),
                    createdBy = currentUser.uid
                )

                val result = firebaseRepository.addStation(station)
                when (result) {
                    is br.com.vigiadeposto.domain.model.Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isSuccess = true,
                            isSubmitting = false
                        )
                    }
                    is br.com.vigiadeposto.domain.model.Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Erro ao adicionar posto: ${result.exception.message}",
                            isSubmitting = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erro inesperado: ${e.message}",
                    isSubmitting = false
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetSuccess() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}

data class AddStationUiState(
    val currentLocation: LatLng? = null,
    val name: String = "",
    val address: String = "",
    val cnpj: String = "",
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val canSubmit: Boolean
        get() = address.isNotEmpty() && !isSubmitting && !isLoading
}
