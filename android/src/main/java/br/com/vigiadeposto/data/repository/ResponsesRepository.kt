package br.com.vigiadeposto.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponsesRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    
    suspend fun carregarRespostas(): Triple<List<String>, List<String>, List<String>> {
        return try {
            val snap = firestore.collection("respostas").document("padrao").get().await()
            val positivo = snap.get("positivo") as? List<String> ?: emptyList()
            val negativo = snap.get("negativo") as? List<String> ?: emptyList()
            val intermediario = snap.get("intermediario") as? List<String> ?: emptyList()
            
            Triple(positivo, negativo, intermediario)
        } catch (e: Exception) {
            // Fallback para respostas padrão em caso de erro
            Triple(
                listOf("Excelente atendimento", "Combustível de qualidade", "Preço justo"),
                listOf("Gasolina ruim", "Atendimento péssimo", "Preço abusivo"),
                listOf("Atendimento ok", "Preço na média", "Local limpo")
            )
        }
    }
}
