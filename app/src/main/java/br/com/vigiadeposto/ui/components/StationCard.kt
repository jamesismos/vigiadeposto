package br.com.vigiadeposto.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.vigiadeposto.domain.model.Station
import br.com.vigiadeposto.domain.model.Vote
import br.com.vigiadeposto.ui.theme.Dimensions

@Composable
fun StationCard(
    station: Station,
    onClick: () -> Unit,
    onVote: ((Int) -> Unit)? = null,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing16)
        ) {
            // Header com nome e botão de fechar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = station.name ?: "Posto",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                onClose?.let {
                    IconButton(onClick = it) {
                        Icon(Icons.Default.Close, "Fechar")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacing8))
            
            // Status e Rating
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing8)
            ) {
                StatusIcon(
                    status = station.status,
                    size = StatusIconSize.Small
                )
                
                if (station.rating > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing4)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = String.format("%.1f", station.rating),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacing8))
            
            // Endereço
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Localização",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(Dimensions.spacing4))
                
                Text(
                    text = station.address ?: "Endereço não informado",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacing12))
            
            // Botões de voto
            if (onVote != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botão positivo
                    OutlinedButton(
                        onClick = { onVote(Vote.POSITIVE) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.ThumbUp,
                            contentDescription = "Positivo",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.spacing4))
                        Text("${station.pos}")
                    }
                    
                    Spacer(modifier = Modifier.width(Dimensions.spacing8))
                    
                    // Botão negativo
                    OutlinedButton(
                        onClick = { onVote(Vote.NEGATIVE) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.ThumbDown,
                            contentDescription = "Negativo",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(Dimensions.spacing4))
                        Text("${station.neg}")
                    }
                }
            }
        }
    }
}
