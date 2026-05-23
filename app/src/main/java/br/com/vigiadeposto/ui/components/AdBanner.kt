package br.com.vigiadeposto.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-4848539261829137/5992548268" // ID de produção
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                setAdUnitId(adUnitId)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun AdBannerLarge(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-4848539261829137/5992548268" // ID de produção
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.LARGE_BANNER)
                setAdUnitId(adUnitId)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun AdBannerSmall(
    modifier: Modifier = Modifier,
    adUnitId: String = "ca-app-pub-4848539261829137/5992548268" // ID de produção
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.SMART_BANNER)
                setAdUnitId(adUnitId)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

/**
 * Componente para carregar e exibir anúncios intersticiais (tela cheia)
 * Use este composable em telas onde você quer mostrar anúncios entre ações
 */
@Composable
fun InterstitialAdLoader(
    adUnitId: String = "ca-app-pub-4848539261829137/7469281463", // Novo ID intersticial
    onAdLoaded: (InterstitialAd) -> Unit = {},
    onAdFailedToLoad: (LoadAdError) -> Unit = {},
    shouldLoad: Boolean = true
) {
    val context = LocalContext.current
    val activity = context as? Activity
    
    DisposableEffect(shouldLoad) {
        if (shouldLoad && activity != null) {
            val adRequest = AdRequest.Builder().build()
            
            InterstitialAd.load(
                context,
                adUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        onAdLoaded(interstitialAd)
                    }
                    
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        onAdFailedToLoad(loadAdError)
                    }
                }
            )
        }
        
        onDispose { }
    }
}

/**
 * Função utilitária para mostrar anúncio intersticial
 * Chame esta função quando quiser exibir o anúncio
 */
fun showInterstitialAd(
    interstitialAd: InterstitialAd?,
    activity: Activity,
    onAdDismissed: () -> Unit = {}
) {
    interstitialAd?.let { ad ->
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onAdDismissed()
            }
        }
        ad.show(activity)
    }
}

/**
 * Componente para carregar anúncios premiados (rewarded ads)
 * Use para dar recompensas aos usuários (pontos, features premium, etc.)
 */
@Composable
fun RewardedAdLoader(
    adUnitId: String = "ca-app-pub-4848539261829137/1570071610", // ID premiado
    onAdLoaded: (RewardedAd) -> Unit = {},
    onAdFailedToLoad: (LoadAdError) -> Unit = {},
    shouldLoad: Boolean = true
) {
    val context = LocalContext.current
    
    DisposableEffect(shouldLoad) {
        if (shouldLoad) {
            val adRequest = AdRequest.Builder().build()
            
            RewardedAd.load(
                context,
                adUnitId,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        onAdLoaded(rewardedAd)
                    }
                    
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        onAdFailedToLoad(loadAdError)
                    }
                }
            )
        }
        
        onDispose { }
    }
}

/**
 * Função para mostrar anúncio premiado
 * O usuário ganha recompensa após assistir o anúncio completo
 */
fun showRewardedAd(
    rewardedAd: RewardedAd?,
    activity: Activity,
    onUserEarnedReward: (rewardType: String, rewardAmount: Int) -> Unit = { _, _ -> },
    onAdDismissed: () -> Unit = {}
) {
    rewardedAd?.let { ad ->
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onAdDismissed()
            }
        }
        
        ad.show(activity, OnUserEarnedRewardListener { rewardItem ->
            // Usuário ganhou a recompensa!
            onUserEarnedReward(rewardItem.type, rewardItem.amount)
        })
    }
}
