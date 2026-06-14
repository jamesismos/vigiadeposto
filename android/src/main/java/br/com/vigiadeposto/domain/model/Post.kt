package br.com.vigiadeposto.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isMonitored: Boolean = false,
    val createdAt: Timestamp? = null
) : Parcelable {
    
    val location: GeoPoint
        get() = GeoPoint(latitude, longitude)
}
