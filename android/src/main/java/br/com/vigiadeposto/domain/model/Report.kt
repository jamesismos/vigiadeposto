package br.com.vigiadeposto.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(
    val id: String = "",
    val stationId: String = "",
    val category: String = "",
    val description: String = "",
    val createdAt: Timestamp? = null
) : Parcelable {

    companion object {
        const val CATEGORY_PRICE = "price"
        const val CATEGORY_QUALITY = "quality"
        const val CATEGORY_SERVICE = "service"
        const val CATEGORY_SAFETY = "safety"
        const val CATEGORY_OTHER = "other"
    }
}
