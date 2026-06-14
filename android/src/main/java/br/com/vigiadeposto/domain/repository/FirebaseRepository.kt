package br.com.vigiadeposto.domain.repository

import br.com.vigiadeposto.domain.model.Result
import br.com.vigiadeposto.domain.model.Station

interface FirebaseRepository {
    suspend fun getStationsNear(lat: Double, lng: Double, radius: Double): List<Station>
    suspend fun addStation(station: Station): Result<String>
    suspend fun voteStation(stationId: String, value: Int): Result<Unit>
    suspend fun voteStationWithText(stationId: String, value: Int, tipo: String, status: String): Result<Unit>
    suspend fun getTopStations(limit: Int): List<Station>
    suspend fun reportStation(stationId: String, category: String, description: String): Result<Unit>
    suspend fun getStationById(stationId: String): Station?
    suspend fun getAllStations(): List<Station>
}
