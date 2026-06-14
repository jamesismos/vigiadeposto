package br.com.vigiadeposto.data.repository

import br.com.vigiadeposto.domain.model.Result
import br.com.vigiadeposto.domain.model.Station
import br.com.vigiadeposto.domain.model.Vote
import br.com.vigiadeposto.domain.repository.FirebaseRepository as FirebaseRepositoryInterface
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirebaseRepositoryInterface {

    private val stationsCollection = firestore.collection("stations")
    private val votesCollection = firestore.collection("votes")
    private val reportsCollection = firestore.collection("reports")

    /**
     * Busca estações próximas usando bounding box (Firebase free não tem geo query nativo)
     * @param lat Latitude central
     * @param lng Longitude central
     * @param radius Raio em quilômetros
     */
    override suspend fun getStationsNear(lat: Double, lng: Double, radius: Double): List<Station> {
        return try {
            // Calcula bounding box aproximado (1 grau ≈ 111km)
            val latDelta = radius / 111.0
            val lngDelta = radius / (111.0 * Math.cos(Math.toRadians(lat)))
            
            val minLat = lat - latDelta
            val maxLat = lat + latDelta
            val minLng = lng - lngDelta
            val maxLng = lng + lngDelta

            val snapshot = stationsCollection
                .whereGreaterThanOrEqualTo("lat", minLat)
                .whereLessThanOrEqualTo("lat", maxLat)
                .whereGreaterThanOrEqualTo("lng", minLng)
                .whereLessThanOrEqualTo("lng", maxLng)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Station::class.java)?.copy(id = doc.id)
            }.filter { station ->
                // Filtro adicional por distância real (Haversine)
                calculateDistance(lat, lng, station.lat ?: 0.0, station.lng ?: 0.0) <= radius
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Adiciona uma nova estação
     */
    override suspend fun addStation(station: Station): Result<String> {
        return try {
            val docRef = stationsCollection.add(station).await()
            Result.Success(docRef.id)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Vota em uma estação usando atomic counters
     */
    override suspend fun voteStation(stationId: String, value: Int): Result<Unit> {
        return voteStationWithText(stationId, value, "", "")
    }

    override suspend fun voteStationWithText(stationId: String, value: Int, tipo: String, status: String): Result<Unit> {
        return try {
            // Cria documento de voto detalhado na coleção 'avaliacoes'
            val vote = Vote(
                stationId = stationId,
                userId = "anonymous", // TODO: Usar user autenticado quando disponível
                value = value,
                tipo = tipo,
                status = status
            )

            // Salva o voto detalhado
            firestore.collection("avaliacoes")
                .add(vote)
                .await()

            // Atualiza contadores atomicamente na estação
            val updateData = when (value) {
                Vote.POSITIVE -> mapOf("pos" to FieldValue.increment(1))
                Vote.NEGATIVE -> mapOf("neg" to FieldValue.increment(1))
                else -> throw IllegalArgumentException("Valor de voto inválido")
            }

            stationsCollection.document(stationId)
                .update(updateData)
                .await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Busca as estações com melhor avaliação
     */
    override suspend fun getTopStations(limit: Int): List<Station> {
        return try {
            val snapshot = stationsCollection
                .orderBy("pos", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Station::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Reporta uma estação
     */
    override suspend fun reportStation(
        stationId: String, 
        category: String, 
        description: String
    ): Result<Unit> {
        return try {
            val report = mapOf(
                "stationId" to stationId,
                "category" to category,
                "description" to description,
                "createdAt" to FieldValue.serverTimestamp()
            )

            reportsCollection.add(report).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    /**
     * Calcula distância entre dois pontos usando fórmula de Haversine
     */
    private fun calculateDistance(
        lat1: Double, 
        lng1: Double, 
        lat2: Double, 
        lng2: Double
    ): Double {
        val r = 6371.0 // Raio da Terra em km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    /**
     * Busca uma estação por ID
     */
    override suspend fun getStationById(stationId: String): Station? {
        return try {
            val doc = stationsCollection.document(stationId).get().await()
            doc.toObject(Station::class.java)?.copy(id = doc.id)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Busca todas as estações
     */
    override suspend fun getAllStations(): List<Station> {
        return try {
            val snapshot = stationsCollection.get().await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Station::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
