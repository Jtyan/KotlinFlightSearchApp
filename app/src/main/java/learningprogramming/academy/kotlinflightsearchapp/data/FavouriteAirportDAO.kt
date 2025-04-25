package learningprogramming.academy.kotlinflightsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteAirportDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteAirport: FavouriteAirport)

    @Query("""DELETE FROM favorite 
            WHERE departure_code = :departureCode 
            AND destination_code = :destinationCode
            """)
    suspend fun delete(departureCode: String, destinationCode: String)

    @Query("SELECT * FROM favorite ORDER BY departure_code ASC")
    fun getAllFavouriteAirports(): Flow<List<FavouriteAirport>>
}