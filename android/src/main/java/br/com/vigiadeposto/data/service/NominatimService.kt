package br.com.vigiadeposto.data.service

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Serviço de Geocodificação usando Nominatim (OpenStreetMap)
 * 100% GRATUITO - Conversão de endereços em coordenadas e vice-versa
 * 
 * Documentação: https://nominatim.org/release-docs/develop/api/Search/
 */
@Singleton
class NominatimService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val httpClient = OkHttpClient()
    
    // Múltiplos endpoints Nominatim para maior confiabilidade
    private val nominatimEndpoints = listOf(
        "https://nominatim.openstreetmap.org",
        "https://nominatim.osm.org"
    )
    
    /**
     * Busca coordenadas a partir de um endereço (Geocoding)
     * Exemplo: "Avenida Paulista, São Paulo" -> LatLng(-23.5619, -46.6556)
     */
    suspend fun searchAddress(
        query: String,
        countryCode: String = "br"
    ): List<NominatimResult> = suspendCancellableCoroutine { continuation ->
        
        Log.d("Nominatim", "Buscando endereço: $query")
        
        try {
            val encodedQuery = java.net.URLEncoder.encode(query, "UTF-8")
            val url = "${nominatimEndpoints[0]}/search" +
                    "?q=$encodedQuery" +
                    "&format=json" +
                    "&addressdetails=1" +
                    "&limit=5" +
                    "&countrycodes=$countryCode" +
                    "&accept-language=pt-BR,pt,en"
            
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "VigiadePosto/1.0 (Android)")
                .build()

            httpClient.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("Nominatim", "Erro na busca de endereço", e)
                    continuation.resume(emptyList())
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    try {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body?.string()
                            val results = parseSearchResponse(jsonResponse)
                            Log.d("Nominatim", "Encontrados ${results.size} resultados para: $query")
                            continuation.resume(results)
                        } else {
                            Log.e("Nominatim", "Resposta não sucesso: ${response.code}")
                            continuation.resume(emptyList())
                        }
                    } catch (e: Exception) {
                        Log.e("Nominatim", "Erro ao processar resposta de busca", e)
                        continuation.resume(emptyList())
                    }
                }
            })
            
        } catch (e: Exception) {
            Log.e("Nominatim", "Erro geral na busca de endereço", e)
            continuation.resume(emptyList())
        }
    }
    
    /**
     * Busca endereço a partir de coordenadas (Reverse Geocoding)
     * Exemplo: LatLng(-23.5619, -46.6556) -> "Avenida Paulista, São Paulo"
     */
    suspend fun reverseGeocode(
        location: LatLng
    ): NominatimResult? = suspendCancellableCoroutine { continuation ->
        
        Log.d("Nominatim", "Reverse geocoding: ${location.latitude}, ${location.longitude}")
        
        try {
            val url = "${nominatimEndpoints[0]}/reverse" +
                    "?lat=${location.latitude}" +
                    "&lon=${location.longitude}" +
                    "&format=json" +
                    "&addressdetails=1" +
                    "&accept-language=pt-BR,pt,en"
            
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "VigiadePosto/1.0 (Android)")
                .build()

            httpClient.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("Nominatim", "Erro no reverse geocoding", e)
                    continuation.resume(null)
                }

                override fun onResponse(call: okhttp3.Call, response: Response) {
                    try {
                        if (response.isSuccessful) {
                            val jsonResponse = response.body?.string()
                            val result = parseReverseResponse(jsonResponse)
                            Log.d("Nominatim", "Reverse geocoding encontrou: ${result?.displayName}")
                            continuation.resume(result)
                        } else {
                            Log.e("Nominatim", "Resposta não sucesso: ${response.code}")
                            continuation.resume(null)
                        }
                    } catch (e: Exception) {
                        Log.e("Nominatim", "Erro ao processar resposta de reverse", e)
                        continuation.resume(null)
                    }
                }
            })
            
        } catch (e: Exception) {
            Log.e("Nominatim", "Erro geral no reverse geocoding", e)
            continuation.resume(null)
        }
    }
    
    private fun parseSearchResponse(jsonResponse: String?): List<NominatimResult> {
        if (jsonResponse == null) return emptyList()
        
        try {
            val jsonArray = JSONArray(jsonResponse)
            val results = mutableListOf<NominatimResult>()
            
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)
                val result = parseNominatimObject(item)
                result?.let { results.add(it) }
            }
            
            return results
            
        } catch (e: Exception) {
            Log.e("Nominatim", "Erro ao fazer parse da resposta de busca", e)
            return emptyList()
        }
    }
    
    private fun parseReverseResponse(jsonResponse: String?): NominatimResult? {
        if (jsonResponse == null) return null
        
        try {
            val jsonObject = JSONObject(jsonResponse)
            return parseNominatimObject(jsonObject)
            
        } catch (e: Exception) {
            Log.e("Nominatim", "Erro ao fazer parse da resposta de reverse", e)
            return null
        }
    }
    
    private fun parseNominatimObject(obj: JSONObject): NominatimResult? {
        try {
            val lat = obj.getDouble("lat")
            val lon = obj.getDouble("lon")
            val displayName = obj.optString("display_name", "")
            val type = obj.optString("type", "")
            val osmType = obj.optString("osm_type", "")
            val osmId = obj.optLong("osm_id", 0)
            
            // Parse endereço detalhado
            val address = obj.optJSONObject("address")
            val street = address?.optString("road") ?: address?.optString("street") ?: ""
            val houseNumber = address?.optString("house_number") ?: ""
            val city = address?.optString("city") ?: address?.optString("town") ?: address?.optString("village") ?: ""
            val state = address?.optString("state") ?: ""
            val postcode = address?.optString("postcode") ?: ""
            val country = address?.optString("country") ?: ""
            
            return NominatimResult(
                latitude = lat,
                longitude = lon,
                displayName = displayName,
                type = type,
                osmType = osmType,
                osmId = osmId,
                street = street,
                houseNumber = houseNumber,
                city = city,
                state = state,
                postcode = postcode,
                country = country
            )
            
        } catch (e: Exception) {
            Log.e("Nominatim", "Erro ao converter objeto Nominatim", e)
            return null
        }
    }
}

/**
 * Resultado da busca/geocodificação Nominatim
 */
data class NominatimResult(
    val latitude: Double,
    val longitude: Double,
    val displayName: String,
    val type: String,
    val osmType: String,
    val osmId: Long,
    val street: String,
    val houseNumber: String,
    val city: String,
    val state: String,
    val postcode: String,
    val country: String
) {
    fun toLatLng(): LatLng = LatLng(latitude, longitude)
    
    fun toGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)
    
    fun getFormattedAddress(): String {
        return buildString {
            if (street.isNotEmpty()) {
                append(street)
                if (houseNumber.isNotEmpty()) append(", $houseNumber")
            }
            if (city.isNotEmpty()) {
                if (isNotEmpty()) append(", ")
                append(city)
            }
            if (state.isNotEmpty()) {
                if (isNotEmpty()) append(", ")
                append(state)
            }
            if (postcode.isNotEmpty()) {
                if (isNotEmpty()) append(" - ")
                append(postcode)
            }
        }.ifEmpty { displayName }
    }
}
