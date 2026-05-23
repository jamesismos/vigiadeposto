package br.com.vigiadeposto

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VigiaDePostoApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        try {
            // Inicializa Google AdMob
            MobileAds.initialize(this) {}
            Log.d("VigiaApp", "AdMob inicializado com sucesso")
        } catch (e: Exception) {
            Log.e("VigiaApp", "Erro ao inicializar AdMob", e)
        }
        
        Log.d("VigiaApp", "Aplicação iniciada com sucesso")
    }
}
