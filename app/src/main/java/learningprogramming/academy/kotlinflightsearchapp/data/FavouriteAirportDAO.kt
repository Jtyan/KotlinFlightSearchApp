package learningprogramming.academy.kotlinflightsearchapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteAirportDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favouriteAirport: FavouriteAirport)

    @Delete
    suspend fun delete(favouriteAirport: FavouriteAirport)

    @Query("SELECT * FROM favourites ORDER BY departure_code ASC")
    fun getAllFavouriteAirports(): Flow<List<FavouriteAirport>>
}