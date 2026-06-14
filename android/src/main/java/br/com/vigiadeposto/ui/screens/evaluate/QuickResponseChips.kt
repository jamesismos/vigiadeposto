package br.com.vigiadeposto.ui.screens.evaluate

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import br.com.vigiadeposto.domain.model.Vote
import br.com.vigiadeposto.ui.theme.Dimensions

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuickResponseChips(
    respostasPositivas: List<String>,
    respostasNegativas: List<String>,
    respostasIntermediarias: List<String>,
    isEnabled: Boolean,
    onResponseSelected: (String, String) -> Unit
) {
    if (respostasPositivas.isEmpty() && respostasNegativas.isEmpty() && respostasIntermediarias.isEmpty()) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Dimensions.spacing12)
    ) {
        Text(
            text = "Respostas rápidas:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = Dimensions.spacing8)
        )

        // Respostas positivas
        if (respostasPositivas.isNotEmpty()) {
            Text(
                text = "👍 Positivas",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = Dimensions.spacing4)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing4),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing4)
            ) {
                respostasPositivas.forEach { resposta ->
                    AssistChip(
                        onClick = { onResponseSelected(resposta, Vote.STATUS_POSITIVO) },
                        enabled = isEnabled,
                        label = { 
                            Text(
                                text = resposta, 
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spacing8))
        }

        // Respostas negativas  
        if (respostasNegativas.isNotEmpty()) {
            Text(
                text = "👎 Negativas",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = Dimensions.spacing4)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing4),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing4)
            ) {
                respostasNegativas.forEach { resposta ->
                    AssistChip(
                        onClick = { onResponseSelected(resposta, Vote.STATUS_NEGATIVO) },
                        enabled = isEnabled,
                        label = { 
                            Text(
                                text = resposta, 
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            labelColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dimensions.spacing8))
        }

        // Respostas intermediárias
        if (respostasIntermediarias.isNotEmpty()) {
            Text(
                text = "⚡ Intermediárias", 
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = Dimensions.spacing4)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimensions.spacing4),
                verticalArrangement = Arrangement.spacedBy(Dimensions.spacing4)
            ) {
                respostasIntermediarias.forEach { resposta ->
                    AssistChip(
                        onClick = { onResponseSelected(resposta, Vote.STATUS_INTERMEDIARIO) },
                        enabled = isEnabled,
                        label = { 
                            Text(
                                text = resposta, 
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) 
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }
        }
    }
}
