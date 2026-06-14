package br.com.vigiadeposto.ui.screens.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.vigiadeposto.ui.theme.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen() {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informações") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = Dimensions.spacing16,
                end = Dimensions.spacing16,
                top = Dimensions.spacing16,
                bottom = 100.dp // Extra padding para navegação inferior
            ),
            verticalArrangement = Arrangement.spacedBy(Dimensions.spacing16)
        ) {
            // Links Oficiais
            item {
                Text(
                    text = "Links Oficiais",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(officialLinks) { link ->
                OfficialLinkCard(
                    link = link,
                    onClick = { uriHandler.openUri(link.url) }
                )
            }
            
            // Dicas
            item {
                Spacer(modifier = Modifier.height(Dimensions.spacing8))
                Text(
                    text = "Dicas de Segurança",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(safetyTips) { tip ->
                SafetyTipCard(tip = tip)
            }
            
            // Apoie o Projeto
            item {
                Spacer(modifier = Modifier.height(Dimensions.spacing8))
                Text(
                    text = "Apoie o Projeto",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                SupportCard()
            }
        }
    }
}

@Composable
private fun OfficialLinkCard(
    link: OfficialLink,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = link.icon,
                contentDescription = link.name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(Dimensions.spacing12))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = link.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = link.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                Icons.Default.OpenInNew,
                contentDescription = "Abrir link",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SafetyTipCard(tip: SafetyTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing16),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = tip.emoji,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.width(Dimensions.spacing12))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tip.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tip.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SupportCard() {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.spacing16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Favorite,
                contentDescription = "Apoio",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(Dimensions.spacing12))
            
            Text(
                text = "Apoie o Desenvolvimento",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(Dimensions.spacing8))
            
            Text(
                text = "Gostou do app? Ajude com uma contribuição para manter o projeto ativo e gratuito!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(Dimensions.spacing16))
            
            // PIX
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.spacing16),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AccountBalance,
                            contentDescription = "PIX",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PIX - James Oliveira",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "72a25509-c296-4f1c-a53d-316623d382b9",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString("72a25509-c296-4f1c-a53d-316623d382b9"))
                            android.widget.Toast.makeText(context, "PIX copiado!", android.widget.Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copiar PIX",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copiar PIX")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(Dimensions.spacing12))
            
            // Bitcoin
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(Dimensions.spacing16),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CurrencyBitcoin,
                            contentDescription = "Bitcoin",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bitcoin",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "bc1qzewr447fjwln66es26qdzkwmpqy3ukfvs89nnz",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString("bc1qzewr447fjwln66es26qdzkwmpqy3ukfvs89nnz"))
                            android.widget.Toast.makeText(context, "Bitcoin copiado!", android.widget.Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "Copiar Bitcoin",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Copiar Bitcoin")
                    }
                }
            }
        }
    }
}

data class OfficialLink(
    val name: String,
    val description: String,
    val url: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

data class SafetyTip(
    val emoji: String,
    val title: String,
    val description: String
)

private val officialLinks = listOf(
    OfficialLink(
        name = "Petrobras",
        description = "Site oficial da Petrobras",
        url = "https://www.petrobras.com.br",
        icon = Icons.Default.Business
    ),
    OfficialLink(
        name = "ANP",
        description = "Agência Nacional do Petróleo",
        url = "https://www.gov.br/anp",
        icon = Icons.Default.Verified
    )
)

private val safetyTips = listOf(
    SafetyTip(
        emoji = "💡",
        title = "Identifique adulteração",
        description = "Observe se o combustível tem cor, cheiro ou consistência anormal. Desconfie de preços muito baixos."
    ),
    SafetyTip(
        emoji = "💡",
        title = "Golpes comuns",
        description = "Cuidado com 'promoções' suspeitas, funcionários que pedem dinheiro extra ou 'testes' de qualidade."
    ),
    SafetyTip(
        emoji = "💡",
        title = "Notícias verificadas",
        description = "Sempre verifique informações em fontes oficiais. Não compartilhe notícias sem confirmação."
    )
)
