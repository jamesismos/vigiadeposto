package br.com.vigiadeposto.ui.screens.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.vigiadeposto.ui.theme.Dimensions
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStationScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddStationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Observa sucesso para voltar
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Posto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Preview do mapa
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(Dimensions.spacing16),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                uiState.currentLocation?.let { location ->
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = rememberCameraPositionState {
                            position = CameraPosition.fromLatLngZoom(location, 15f)
                        },
                        properties = MapProperties(
                            isMyLocationEnabled = true
                        )
                    ) {
                        Marker(
                            state = MarkerState(position = location),
                            title = "Localização atual"
                        )
                    }
                } ?: run {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator()
                        } else {
                            Text("Carregando localização...")
                        }
                    }
                }
            }

            // Formulário
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimensions.spacing16)
            ) {
                Text(
                    text = "Informações do Posto",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing16))
                
                // Nome (opcional)
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.updateName(it) },
                    label = { Text("Nome do Posto (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Business, "Nome") },
                    enabled = !uiState.isSubmitting
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing12))
                
                // Endereço
                OutlinedTextField(
                    value = uiState.address,
                    onValueChange = { viewModel.updateAddress(it) },
                    label = { Text("Endereço *") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.LocationOn, "Endereço") },
                    enabled = !uiState.isSubmitting,
                    isError = uiState.address.isEmpty() && !uiState.isLoading
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing12))
                
                // CNPJ (opcional)
                OutlinedTextField(
                    value = uiState.cnpj,
                    onValueChange = { viewModel.updateCnpj(it) },
                    label = { Text("CNPJ (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    leadingIcon = { Icon(Icons.Default.Numbers, "CNPJ") },
                    enabled = !uiState.isSubmitting
                )
                
                Spacer(modifier = Modifier.height(Dimensions.spacing24))
                
                // Botão de adicionar
                Button(
                    onClick = { viewModel.addStation() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.canSubmit
                ) {
                    if (uiState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Add, "Adicionar")
                    }
                    Spacer(modifier = Modifier.width(Dimensions.spacing8))
                    Text("Adicionar Posto")
                }
                
                // Mensagem de erro
                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(Dimensions.spacing16))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = uiState.errorMessage!!,
                            modifier = Modifier.padding(Dimensions.spacing12),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Informações adicionais
                Spacer(modifier = Modifier.height(Dimensions.spacing24))
                
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(Dimensions.spacing16)
                    ) {
                        Text(
                            text = "ℹ️ Informações",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(Dimensions.spacing8))
                        
                        Text(
                            text = "• A localização será detectada automaticamente\n" +
                                    "• O endereço será preenchido com base na localização\n" +
                                    "• Nome e CNPJ são opcionais\n" +
                                    "• Você pode editar o endereço se necessário",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
