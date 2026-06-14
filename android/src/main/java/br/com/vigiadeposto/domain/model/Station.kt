package br.com.vigiadeposto.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Station(
    val id: String = "",
    val name: String? = null,
    val address: String? = null,
    val location: @RawValue GeoPoint? = null,
    val pos: Int = 0,
    val neg: Int = 0,
    val createdBy: String? = null,
    val createdAt: @RawValue Timestamp? = null,
    
    // Campos do Google Places
    val phone: String? = null,
    val rating: Float = 0f, // Avaliação do Google
    val totalRatings: Int = 0, // Total de avaliações do Google
    val isFromGooglePlaces: Boolean = false,
    val priceLevel: Int = 0, // 0-4 scale do Google
    val isOpen: Boolean = true,
    val status: Status = Status.GREY // Status manual ou computado
) : Parcelable {
    
    // Propriedades computadas (mantidas para compatibilidade)
    val computedStatus: Status
        get() = if (isFromGooglePlaces) {
            status // Usa o status já definido pelo Google Places
        } else {
            Status.computeStatus(pos, neg) // Computa baseado em votos
        }
    
    val computedRating: Double
        get() = if (isFromGooglePlaces && rating > 0) {
            rating.toDouble() // Usa rating do Google
        } else if (pos + neg > 0) {
            (pos.toDouble() / (pos + neg)) * 5.0 // Computa baseado em votos
        } else {
            0.0
        }
    
    val lat: Double?
        get() = location?.latitude
    
    val lng: Double?
        get() = location?.longitude
}
