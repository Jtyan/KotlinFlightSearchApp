package learningprogramming.academy.kotlinflightsearchapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, FavouriteAirport::class], version = 1, exportSchema = false)
abstract class FlightSearchDatabase : RoomDatabase() {
    abstract fun airportDAO(): AirportDAO
    abstract fun favouriteAirportDAO(): FavouriteAirportDAO

    companion object {
        @Volatile
        private var instance: FlightSearchDatabase? = null

        fun getDatabase(context: Context): FlightSearchDatabase {
            return instance ?: synchronized(this){
                Room.databaseBuilder(
                    context = context.applicationContext,
                    FlightSearchDatabase::class.java,
                    "flight_search_database")
                    .createFromAsset("database/flight_search.db")
                    .build()
                    .also { instance = it }
            }
        }
    }
}