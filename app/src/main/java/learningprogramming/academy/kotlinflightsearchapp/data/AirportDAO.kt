package learningprogramming.academy.kotlinflightsearchapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDAO {
    @Query("SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("""SELECT * FROM airport
            WHERE LOWER(name) LIKE '%' || TRIM(LOWER(:query)) || '%'
            OR LOWER(iata_code) LIKE '%' || TRIM(LOWER(:query)) || '%'
            ORDER BY passengers DESC
            """)
    fun searchAirports (query: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    fun getDestinationsFrom(departureCode: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code = :code LIMIT 1")
    fun getAirportByCode(code: String): Flow<Airport>
}