package br.com.vigiadeposto.domain.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vote(
    val id: String = "",
    val stationId: String = "",
    val userId: String = "",
    val value: Int = 0, // -1 para negativo, 1 para positivo
    val tipo: String = "", // Texto escolhido (ex: "Gasolina ruim")
    val status: String = "", // "positivo" | "negativo" | "intermediario"
    @ServerTimestamp
    val createdAt: Timestamp? = null
) : Parcelable {
    
    companion object {
        const val POSITIVE = 1
        const val NEGATIVE = -1
        
        const val STATUS_POSITIVO = "positivo"
        const val STATUS_NEGATIVO = "negativo"  
        const val STATUS_INTERMEDIARIO = "intermediario"
    }
}
