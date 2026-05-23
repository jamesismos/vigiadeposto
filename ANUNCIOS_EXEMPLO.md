# 🎯 Como Usar os Anúncios - Vigia de Posto

## 📱 IDs Configurados

### Banner Ads (IDs configurados)
- **Banner ID**: `ca-app-pub-4848539261829137/5992548268`
- **Intersticial ID**: `ca-app-pub-4848539261829137/7469281463`

## 🎨 Tipos de Anúncios Disponíveis

### 1. 📰 Banner Ads (Já implementado)
```kotlin
import br.com.vigiadeposto.ui.components.AdBanner

// Banner normal
AdBanner()

// Banner grande
AdBannerLarge() 

// Smart banner
AdBannerSmall()
```

### 2. 🎬 Intersticial Ads (Novo!)
```kotlin
import br.com.vigiadeposto.ui.components.InterstitialAdLoader
import br.com.vigiadeposto.ui.components.showInterstitialAd

@Composable
fun MinhaScreen() {
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    val context = LocalContext.current
    val activity = context as? Activity
    
    // Carrega o anúncio
    InterstitialAdLoader(
        onAdLoaded = { ad -> 
            interstitialAd = ad 
        }
    )
    
    Button(
        onClick = {
            // Mostra anúncio quando usuário clica
            activity?.let { 
                showInterstitialAd(
                    interstitialAd = interstitialAd,
                    activity = it,
                    onAdDismissed = {
                        // Recarrega anúncio para próxima vez
                        interstitialAd = null
                    }
                )
            }
        }
    ) {
        Text("Finalizar Avaliação")
    }
}
```

## 💡 Estratégias de Monetização

### 📍 Onde usar Banner Ads:
- ✅ **Tela de Ranking** (já implementado)
- ✅ **Tela de Mapa** (no topo ou embaixo)
- ✅ **Tela de Info** (final da tela)
- ✅ **Entre cards da lista**

### 🎬 Onde usar Intersticial Ads:
- ✅ **Após avaliar um posto** (maior engajamento)
- ✅ **Ao navegar entre telas principais**
- ✅ **Após adicionar novo posto**
- ✅ **A cada 3-5 ações do usuário**

### ⚡ Exemplo Prático - Tela de Avaliação:
```kotlin
@Composable
fun EvaluateScreen() {
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    val activity = LocalContext.current as? Activity
    
    // Carrega anúncio ao entrar na tela
    InterstitialAdLoader(
        onAdLoaded = { interstitialAd = it }
    )
    
    // Quando usuário termina avaliação
    Button(
        onClick = {
            // 1. Salva avaliação
            viewModel.submitVote()
            
            // 2. Mostra anúncio
            activity?.let {
                showInterstitialAd(
                    interstitialAd = interstitialAd,
                    activity = it,
                    onAdDismissed = {
                        // 3. Navega de volta
                        onNavigateBack()
                    }
                )
            }
        }
    ) {
        Text("Enviar Avaliação")
    }
}
```

## 💰 Máximo de Receita

### 🎯 Frequência Recomendada:
- **Banner**: Sempre visível nas telas principais
- **Intersticial**: A cada 2-3 minutos de uso ativo
- **Evitar**: Anúncios demais (irritam usuário)

### 📊 Métricas para Acompanhar:
- **CTR (Click-through rate)**: Taxa de cliques
- **eCPM**: Receita por mil impressões  
- **Fill Rate**: % de anúncios exibidos com sucesso
- **Retenção de usuários**: Não perder usuários por excesso de ads

## 🚀 Implementação Rápida

Para adicionar intersticial em qualquer tela:

1. **Import**:
```kotlin
import br.com.vigiadeposto.ui.components.InterstitialAdLoader
import br.com.vigiadeposto.ui.components.showInterstitialAd
```

2. **State**:
```kotlin
var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
```

3. **Carregar**:
```kotlin
InterstitialAdLoader(onAdLoaded = { interstitialAd = it })
```

4. **Mostrar**:
```kotlin
showInterstitialAd(interstitialAd, activity) { /* callback */ }
```

## ✅ Status Atual
- [x] Banner no Ranking
- [ ] Intersticial nas avaliações  
- [ ] Banner nas outras telas
- [ ] Intersticial na navegação
