package br.com.vigiadeposto.ui.screens.ranking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.vigiadeposto.domain.model.Station
import br.com.vigiadeposto.ui.components.*
import br.com.vigiadeposto.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RankingScreen(
    onNavigateToStationDetails: (String) -> Unit,
    viewModel: RankingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ranking de Postos") },
                actions = {
                    // Filtro por região
                    var expanded by remember { mutableStateOf(false) }
                    val regions = listOf("Todas", "São Paulo", "Rio de Janeiro", "Belo Horizonte", "Brasília")
                    
                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.FilterList, "Filtrar por região")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            regions.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region) },
                                    onClick = {
                                        viewModel.setRegionFilter(if (region == "Todas") null else region)
                                        expanded = false
                                    }
                                )
                            }
                        }
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
            // Abas
            TabRow(
                selectedTabIndex = if (uiState.selectedTab == RankingTab.TOP) 0 else 1
            ) {
                Tab(
                    selected = uiState.selectedTab == RankingTab.TOP,
                    onClick = { viewModel.setSelectedTab(RankingTab.TOP) },
                    text = { Text("🏆 Melhores") },
                    icon = { Icon(Icons.Default.Star, "Melhores") }
                )
                Tab(
                    selected = uiState.selectedTab == RankingTab.BOTTOM,
                    onClick = { viewModel.setSelectedTab(RankingTab.BOTTOM) },
                    text = { Text("⚠️ Piores") },
                    icon = { Icon(Icons.Default.Warning, "Piores") }
                )
            }

            // Lista de estações
            if (uiState.isLoadingCurrent) {
                LoadingScreen()
            } else if (uiState.errorMessage != null) {
                ErrorScreen(
                    message = uiState.errorMessage!!,
                    onRetry = {
                        when (uiState.selectedTab) {
                            RankingTab.TOP -> viewModel.loadTopStations()
                            RankingTab.BOTTOM -> viewModel.loadBottomStations()
                        }
                    }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Dimensions.spacing16),
                    verticalArrangement = Arrangement.spacedBy(Dimensions.spacing12)
                ) {
                    itemsIndexed(uiState.currentStations) { index, station ->
                        RankingStationCard(
                            station = station,
                            position = index + 1,
                            onClick = { onNavigateToStationDetails(station.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RankingStationCard(
    station: Station,
    position: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posição
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = when (position) {
                            1 -> MaterialTheme.colorScheme.tertiary
                            2 -> MaterialTheme.colorScheme.outline
                            3 -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$position",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (position) {
                        1, 2, 3 -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Spacer(modifier = Modifier.width(Dimensions.spacing12))

            // Informações da estação
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = station.name ?: "Posto",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing4))
                
                Text(
                    text = station.address ?: "Endereço não informado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing8))
                
                // Rating e votos
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing8)
                ) {
                    // Estrelas
                    if (station.rating > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = String.format("%.1f", station.rating),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    
                    // Votos
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing4)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.ThumbUp,
                                contentDescription = "Positivos",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${station.pos}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.ThumbDown,
                                contentDescription = "Negativos",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${station.neg}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // Status
            StatusIcon(
                status = station.status,
                size = StatusIconSize.Medium
            )
        }
    }
}
