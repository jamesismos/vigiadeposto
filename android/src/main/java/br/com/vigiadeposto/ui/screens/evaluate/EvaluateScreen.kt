package br.com.vigiadeposto.ui.screens.evaluate

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.vigiadeposto.domain.model.Status
import br.com.vigiadeposto.domain.model.Vote
import br.com.vigiadeposto.ui.components.*
import br.com.vigiadeposto.ui.theme.Dimensions
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun EvaluateScreen(
    onNavigateToStationDetails: (String) -> Unit,
    viewModel: EvaluateViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(uiState.isLoading)
    
    // Permissão de localização
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    
    // Estado para controlar o dialog de permissão
    var showPermissionDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(locationPermissionState.status) {
        val status = locationPermissionState.status
        when (status) {
            PermissionStatus.Granted -> {
                viewModel.loadStations()
            }
            is PermissionStatus.Denied -> {
                if (status.shouldShowRationale) {
                    showPermissionDialog = true
                } else {
                    locationPermissionState.launchPermissionRequest()
                }
            }
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            // Mostrar snackbar ou toast
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avaliar Postos") },
                actions = {
                    IconButton(onClick = { viewModel.loadStations() }) {
                        Icon(Icons.Default.Refresh, "Atualizar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros de status
            StatusFilterBar(
                selectedStatus = uiState.statusFilter,
                onStatusSelected = { viewModel.setStatusFilter(it) }
            )

            // Lista de estações
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.loadStations() }
            ) {
                if (uiState.isLoading && uiState.stations.isEmpty()) {
                    LoadingScreen()
                } else if (uiState.errorMessage != null && uiState.stations.isEmpty()) {
                    ErrorScreen(
                        message = uiState.errorMessage!!,
                        onRetry = { viewModel.loadStations() }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(Dimensions.spacing16),
                        verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
                    ) {
                        items(uiState.filteredStations) { station ->
                            EvaluateStationCard(
                                station = station,
                                isVoting = uiState.votingStations.contains(station.id),
                                respostasPositivas = uiState.respostasPositivas,
                                respostasNegativas = uiState.respostasNegativas,
                                respostasIntermediarias = uiState.respostasIntermediarias,
                                onVote = { voteValue ->
                                    viewModel.voteStation(station.id, voteValue)
                                },
                                onVoteWithText = { voteValue, tipo, status ->
                                    viewModel.voteStationWithText(station.id, voteValue, tipo, status)
                                },
                                onClick = { onNavigateToStationDetails(station.id) }
                            )
                        }
                    }
                }
            }
        }

        // Feedback de voto bem-sucedido
        if (uiState.showVoteSuccess) {
            Snackbar(
                modifier = Modifier.padding(Dimensions.spacing16),
                action = {
                    TextButton(onClick = { /* Fechar */ }) {
                        Text("OK")
                    }
                }
            ) {
                Text("Voto registrado com sucesso! 👍")
            }
        }
        
        // Dialog de permissão de localização
        if (showPermissionDialog) {
            LocationPermissionDialog(
                onGrantPermission = {
                    showPermissionDialog = false
                    locationPermissionState.launchPermissionRequest()
                },
                onDismiss = {
                    showPermissionDialog = false
                }
            )
        }
    }
}

@Composable
private fun LocationPermissionDialog(
    onGrantPermission: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Permissão de Localização")
        },
        text = {
            Text(
                "O app precisa acessar sua localização para encontrar postos próximos a você.\n\n" +
                "Toque em 'Permitir' para continuar."
            )
        },
        confirmButton = {
            TextButton(onClick = onGrantPermission) {
                Text("Permitir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Agora não")
            }
        },
        icon = {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Localização",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Composable
private fun StatusFilterBar(
    selectedStatus: Status?,
    onStatusSelected: (Status?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.spacing16),
        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing8)
    ) {
        FilterChip(
            selected = selectedStatus == null,
            onClick = { onStatusSelected(null) },
            label = { Text("Todos") }
        )
        
        FilterChip(
            selected = selectedStatus == Status.GREEN,
            onClick = { onStatusSelected(Status.GREEN) },
            label = { Text("Verde") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
        
        FilterChip(
            selected = selectedStatus == Status.YELLOW,
            onClick = { onStatusSelected(Status.YELLOW) },
            label = { Text("Amarelo") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        )
        
        FilterChip(
            selected = selectedStatus == Status.RED,
            onClick = { onStatusSelected(Status.RED) },
            label = { Text("Vermelho") },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.errorContainer
            )
        )
    }
}

@Composable
private fun EvaluateStationCard(
    station: br.com.vigiadeposto.domain.model.Station,
    isVoting: Boolean,
    respostasPositivas: List<String> = emptyList(),
    respostasNegativas: List<String> = emptyList(),
    respostasIntermediarias: List<String> = emptyList(),
    onVote: (Int) -> Unit,
    onVoteWithText: (Int, String, String) -> Unit = { _, _, _ -> },
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing16)
        ) {
            // Header com nome e status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = station.name ?: "Posto",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                StatusIcon(
                    status = station.status,
                    size = StatusIconSize.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacing8))
            
            // Endereço
            Text(
                text = station.address ?: "Endereço não informado",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(Dimensions.spacing12))
            
            // Botões de voto grandes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
            ) {
                // Botão positivo
                Button(
                    onClick = { onVote(Vote.POSITIVE) },
                    enabled = !isVoting,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isVoting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = "Positivo",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.spacing4))
                    Text("${station.pos}")
                }
                
                // Botão negativo
                Button(
                    onClick = { onVote(Vote.NEGATIVE) },
                    enabled = !isVoting,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    if (isVoting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onError,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            Icons.Default.ThumbDown,
                            contentDescription = "Negativo",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(Dimensions.spacing4))
                    Text("${station.neg}")
                }
            }
            
            // Respostas rápidas
            QuickResponseChips(
                respostasPositivas = respostasPositivas,
                respostasNegativas = respostasNegativas,
                respostasIntermediarias = respostasIntermediarias,
                isEnabled = !isVoting,
                onResponseSelected = { response, status ->
                    val voteValue = when (status) {
                        Vote.STATUS_POSITIVO -> Vote.POSITIVE
                        Vote.STATUS_NEGATIVO -> Vote.NEGATIVE
                        else -> 0 // intermediário não conta para contadores
                    }
                    onVoteWithText(voteValue, response, status)
                }
            )
        }
    }
}
