# 🎁 Anúncios Premiados - Máxima Monetização

## 💰 **IDs Configurados - PRODUÇÃO**

### 🏆 Anúncio Premiado (Rewarded)
- **App ID**: `ca-app-pub-4848539261829137~4175251323`
- **Rewarded ID**: `ca-app-pub-4848539261829137/1570071610`

## 🚀 **Como Implementar - Exemplo Prático**

### 📍 Exemplo 1: Recompensa por Avaliação
```kotlin
@Composable
fun EvaluateScreen() {
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    var userPoints by remember { mutableStateOf(0) }
    val activity = LocalContext.current as? Activity
    
    // Carrega anúncio premiado
    RewardedAdLoader(
        onAdLoaded = { rewardedAd = it }
    )
    
    Column {
        Text("Seus pontos: $userPoints")
        
        // Botão normal de avaliar
        Button(
            onClick = { 
                viewModel.submitVote()
                // Dá +1 ponto normal
                userPoints += 1
            }
        ) {
            Text("Avaliar Posto (+1 ponto)")
        }
        
        // Botão PREMIADO - mais pontos assistindo anúncio
        Button(
            onClick = {
                activity?.let {
                    showRewardedAd(
                        rewardedAd = rewardedAd,
                        activity = it,
                        onUserEarnedReward = { type, amount ->
                            // Usuário assistiu anúncio completo!
                            viewModel.submitVote()
                            userPoints += 5 // 5x mais pontos!
                        }
                    )
                }
            },
            enabled = rewardedAd != null
        ) {
            Text("🎁 Avaliar + Anúncio (+5 pontos!)")
        }
    }
}
```

### 🗺️ Exemplo 2: Ver Mais Postos no Mapa
```kotlin
@Composable
fun MapScreen() {
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    var showPremiumStations by remember { mutableStateOf(false) }
    
    RewardedAdLoader(onAdLoaded = { rewardedAd = it })
    
    // Botão para desbloquear mais postos
    FloatingActionButton(
        onClick = {
            showRewardedAd(
                rewardedAd = rewardedAd,
                activity = activity,
                onUserEarnedReward = { _, _ ->
                    // Desbloqueou feature premium por 24h
                    showPremiumStations = true
                    // Salvar no SharedPreferences com timestamp
                }
            )
        }
    ) {
        Icon(Icons.Default.Star, "Ver Postos Premium")
    }
}
```

### 🏆 Exemplo 3: Sistema de Ranking Premiado
```kotlin
@Composable
fun RankingScreen() {
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    var canSeeFullRanking by remember { mutableStateOf(false) }
    
    RewardedAdLoader(onAdLoaded = { rewardedAd = it })
    
    LazyColumn {
        // Mostra só top 10 normalmente
        itemsIndexed(
            if (canSeeFullRanking) allStations else stations.take(10)
        ) { index, station ->
            RankingCard(station, index + 1)
        }
        
        // Botão para ver ranking completo
        if (!canSeeFullRanking && stations.size > 10) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showRewardedAd(
                                rewardedAd = rewardedAd,
                                activity = activity,
                                onUserEarnedReward = { _, _ ->
                                    canSeeFullRanking = true
                                }
                            )
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.PlayArrow, null)
                        Text("🎁 Assistir anúncio")
                        Text("Ver ranking completo")
                    }
                }
            }
        }
    }
}
```

## 💡 **Estratégias de Monetização Máxima**

### 🎯 **Onde usar Anúncios Premiados:**
1. **Pontos/Moedas extras** por avaliar postos
2. **Desbloquear features premium** temporariamente
3. **Ver mais resultados** no mapa/ranking
4. **Remover anúncios** por algumas horas
5. **Hints/dicas** sobre melhores postos
6. **Badges especiais** no perfil

### 💰 **Por que Premiados geram mais receita:**
- **eCPM mais alto**: Até 3-10x mais que banners
- **100% viewability**: Usuário assiste anúncio completo
- **Maior engajamento**: Usuário quer a recompensa
- **Retention melhor**: Usuário volta para mais recompensas

### ⚡ **Sistema de Pontos Sugerido:**
```kotlin
// Ações normais
avaliarPosto() // +1 ponto
adicionarPosto() // +3 pontos
compartilharApp() // +2 pontos

// Com anúncio premiado (5x multiplicador)
avaliarPosto() + assistirAnuncio() // +5 pontos
adicionarPosto() + assistirAnuncio() // +15 pontos
```

### 🛡️ **Proteções Anti-Spam:**
```kotlin
// Limitar anúncios premiados
val maxRewardedAdsPerDay = 10
val lastAdTimestamp = getLastRewardedAdTime()
val canShowAd = (System.currentTimeMillis() - lastAdTimestamp) > 300000 // 5 min

if (canShowAd && todayAdsCount < maxRewardedAdsPerDay) {
    showRewardedAd(...)
}
```

## 🎮 **Implementação Rápida:**

1. **Import**:
```kotlin
import br.com.vigiadeposto.ui.components.RewardedAdLoader
import br.com.vigiadeposto.ui.components.showRewardedAd
```

2. **State**:
```kotlin
var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
```

3. **Carregar**:
```kotlin
RewardedAdLoader(onAdLoaded = { rewardedAd = it })
```

4. **Mostrar + Recompensar**:
```kotlin
showRewardedAd(rewardedAd, activity) { type, amount ->
    // Dar recompensa ao usuário!
    userPoints += amount * 5
}
```

## 📊 **Resumo dos 3 Tipos de Anúncios:**

| Tipo | ID | Receita | Quando Usar |
|------|----|---------| ------------|
| 📰 **Banner** | `5992548268` | Baixa | Sempre visível |
| 🎬 **Intersticial** | `7469281463` | Média | Entre ações |
| 🏆 **Premiado** | `1570071610` | **ALTA** | Recompensas |

**Combinando os 3 tipos = Receita máxima!** 💰🚀
