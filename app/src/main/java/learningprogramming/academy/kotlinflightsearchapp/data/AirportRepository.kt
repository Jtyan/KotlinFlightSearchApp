package learningprogramming.academy.kotlinflightsearchapp.data

import kotlinx.coroutines.flow.Flow

interface AirportRepository {

    fun getAllAirportsStream(): Flow<List<Airport>>

    fun searchAirportsStream(query: String): Flow<List<Airport>>

    fun getDestinationsFrom(iataCode: String): Flow<List<Airport>>

    fun getAllFavouriteAirportsStream(): Flow<List<FavouriteAirport>>

    suspend fun addFavouriteAirport(favouriteAirport: FavouriteAirport)

    suspend fun removeFavouriteAirport(departureCode: String, destinationCode: String)

    fun getAirportByCode(code: String): Flow<Airport?>
}
