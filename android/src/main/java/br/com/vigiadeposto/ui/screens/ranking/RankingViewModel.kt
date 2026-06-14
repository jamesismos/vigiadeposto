package br.com.vigiadeposto.ui.screens.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.vigiadeposto.domain.model.Station
import br.com.vigiadeposto.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState.asStateFlow()

    init {
        loadTopStations()
    }

    fun loadTopStations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val stations = firebaseRepository.getTopStations(50)
                _uiState.value = _uiState.value.copy(
                    topStations = stations,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erro ao carregar melhores estações: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun loadBottomStations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingBottom = true)
            
            try {
                val allStations = firebaseRepository.getAllStations()
                // Ordena por pior rating (menos votos positivos)
                val bottomStations = allStations
                    .filter { it.pos + it.neg > 0 } // Só estações com votos
                    .sortedBy { it.rating }
                    .take(50)
                
                _uiState.value = _uiState.value.copy(
                    bottomStations = bottomStations,
                    isLoadingBottom = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erro ao carregar piores estações: ${e.message}",
                    isLoadingBottom = false
                )
            }
        }
    }

    fun setSelectedTab(tab: RankingTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
        
        when (tab) {
            RankingTab.TOP -> {
                if (_uiState.value.topStations.isEmpty()) {
                    loadTopStations()
                }
            }
            RankingTab.BOTTOM -> {
                if (_uiState.value.bottomStations.isEmpty()) {
                    loadBottomStations()
                }
            }
        }
    }

    fun setRegionFilter(region: String?) {
        _uiState.value = _uiState.value.copy(selectedRegion = region)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

enum class RankingTab {
    TOP, BOTTOM
}

data class RankingUiState(
    val topStations: List<Station> = emptyList(),
    val bottomStations: List<Station> = emptyList(),
    val selectedTab: RankingTab = RankingTab.TOP,
    val selectedRegion: String? = null,
    val isLoading: Boolean = false,
    val isLoadingBottom: Boolean = false,
    val errorMessage: String? = null
) {
    val currentStations: List<Station>
        get() = when (selectedTab) {
            RankingTab.TOP -> topStations
            RankingTab.BOTTOM -> bottomStations
        }
    
    val isLoadingCurrent: Boolean
        get() = when (selectedTab) {
            RankingTab.TOP -> isLoading
            RankingTab.BOTTOM -> isLoadingBottom
        }
}
