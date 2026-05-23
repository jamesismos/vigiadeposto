package br.com.vigiadeposto.ui.screens.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.vigiadeposto.domain.model.Station
import br.com.vigiadeposto.ui.components.*
import br.com.vigiadeposto.ui.theme.Dimensions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    onNavigateToAddStation: () -> Unit,
    onNavigateToStationDetails: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Permissão de localização
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Estado para controlar o dialog de permissão
    var showPermissionDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(locationPermissionState.status) {
        val status = locationPermissionState.status
        when (status) {
            com.google.accompanist.permissions.PermissionStatus.Granted -> {
                viewModel.loadCurrentLocation()
            }
            is com.google.accompanist.permissions.PermissionStatus.Denied -> {
                if (status.shouldShowRationale) {
                    // Usuário negou, mas pode tentar novamente
                    showPermissionDialog = true
                } else {
                    // Primeira vez ou "nunca mais perguntar" - tenta solicitar
                    locationPermissionState.launchPermissionRequest()
                }
            }
        }
    }

    LaunchedEffect(uiState.currentLocation) {
        uiState.currentLocation?.let { location ->
            viewModel.loadNearbyStations(location.latitude, location.longitude)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de Postos") },
                actions = {
                    IconButton(onClick = { viewModel.loadCurrentLocation() }) {
                        Icon(Icons.Default.MyLocation, "Minha localização")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddStation) {
                Icon(Icons.Default.Add, "Adicionar Posto")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de pesquisa
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.spacing16),
                onSearch = { query ->
                    viewModel.setSearchQuery(query)
                    if (query.isNotEmpty()) {
                        viewModel.searchStations(query)
                    } else {
                        viewModel.clearSearch()
                    }
                },
                onClear = { viewModel.clearSearch() }
            )

            // Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        uiState.currentLocation ?: LatLng(-23.5505, -46.6333), // São Paulo
                        12f
                    )
                },
                properties = MapProperties(
                    isMyLocationEnabled = locationPermissionState.status == com.google.accompanist.permissions.PermissionStatus.Granted
                )
            ) {
                // Marcadores das estações
                uiState.stations.forEach { station ->
                    station.lat?.let { lat ->
                        station.lng?.let { lng ->
                            Marker(
                                state = MarkerState(position = LatLng(lat, lng)),
                                title = station.name ?: "Posto",
                                snippet = station.address,
                                icon = BitmapDescriptorFactory.defaultMarker(
                                    when (station.status) {
                                        br.com.vigiadeposto.domain.model.Status.GREEN -> BitmapDescriptorFactory.HUE_GREEN
                                        br.com.vigiadeposto.domain.model.Status.YELLOW -> BitmapDescriptorFactory.HUE_YELLOW
                                        br.com.vigiadeposto.domain.model.Status.RED -> BitmapDescriptorFactory.HUE_RED
                                        br.com.vigiadeposto.domain.model.Status.GREY -> BitmapDescriptorFactory.HUE_AZURE
                                    }
                                ),
                                onClick = {
                                    viewModel.onStationSelected(station)
                                    true
                                }
                            )
                        }
                    }
                }
            }

            // Estados de loading e erro
            when {
                uiState.isLoading -> {
                    LoadingScreen()
                }
                uiState.errorMessage != null -> {
                    ErrorScreen(
                        message = uiState.errorMessage!!,
                        onRetry = { viewModel.loadCurrentLocation() }
                    )
                }
            }

            // Resultados da pesquisa
            if (uiState.searchResults.isNotEmpty()) {
                SearchResults(
                    stations = uiState.searchResults,
                    onStationClick = { station ->
                        onNavigateToStationDetails(station.id)
                    },
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 80.dp)
                )
            }

            // Estação selecionada
            uiState.selectedStation?.let { station ->
                StationCard(
                    station = station,
                    onClick = { onNavigateToStationDetails(station.id) },
                    onClose = { viewModel.clearSelectedStation() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(Dimensions.spacing16)
                )
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
                "O app precisa acessar sua localização para mostrar postos de gasolina próximos a você.\n\n" +
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
private fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    onClear: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = { 
            query = it
            onSearch(it)
        },
        modifier = modifier,
        placeholder = { Text("Buscar postos...") },
        leadingIcon = { Icon(Icons.Default.Search, "Buscar") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { 
                    query = ""
                    onClear()
                }) {
                    Icon(Icons.Default.Clear, "Limpar")
                }
            }
        },
        singleLine = true
    )
}

@Composable
private fun SearchResults(
    stations: List<Station>,
    onStationClick: (Station) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(Dimensions.spacing8)
        ) {
            items(stations) { station ->
                StationListItem(
                    station = station,
                    onClick = { onStationClick(station) }
                )
            }
        }
    }
}

@Composable
private fun StationListItem(
    station: Station,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(station.name ?: "Posto") },
        supportingContent = { Text(station.address ?: "") },
        leadingContent = {
            StatusIcon(
                status = station.status,
                size = StatusIconSize.Small
            )
        },
        modifier = Modifier.clickable { onClick() }
    )
}
