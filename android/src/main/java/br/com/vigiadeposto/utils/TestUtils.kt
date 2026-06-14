package br.com.vigiadeposto.utils

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.MapsInitializer

object TestUtils {
    private const val TAG = "TestUtils"
    
    fun testFirebaseConnection() {
        try {
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            
            Log.d(TAG, "Firebase Auth: ${auth.app.name}")
            Log.d(TAG, "Firestore: ${firestore.app.name}")
            Log.d(TAG, "Firebase connection test: SUCCESS")
        } catch (e: Exception) {
            Log.e(TAG, "Firebase connection test: FAILED", e)
        }
    }
    
    fun testMapsConnection(context: Context) {
        try {
            MapsInitializer.initialize(context)
            Log.d(TAG, "Google Maps connection test: SUCCESS")
        } catch (e: Exception) {
            Log.e(TAG, "Google Maps connection test: FAILED", e)
        }
    }
    
    fun testNetworkUtils(context: Context) {
        try {
            val networkUtils = NetworkUtils(context)
            val isNetworkAvailable = networkUtils.isNetworkAvailable()
            Log.d(TAG, "Network available: $isNetworkAvailable")
            Log.d(TAG, "NetworkUtils test: SUCCESS")
        } catch (e: Exception) {
            Log.e(TAG, "NetworkUtils test: FAILED", e)
        }
    }
    
    fun runAllTests(context: Context) {
        Log.d(TAG, "=== Starting all tests ===")
        testFirebaseConnection()
        testMapsConnection(context)
        testNetworkUtils(context)
        Log.d(TAG, "=== All tests completed ===")
    }
}
