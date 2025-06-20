package learningprogramming.academy.kotlinflightsearchapp.data

import kotlinx.coroutines.flow.Flow

class AirportRepositoryImpl(
    private val airportDAO: AirportDAO,
    private val favouriteAirportDAO: FavouriteAirportDAO
): AirportRepository {
    override fun getAllAirportsStream(): Flow<List<Airport>> {
        return airportDAO.getAllAirports()
    }

    override fun searchAirportsStream(query: String): Flow<List<Airport>> {
        return airportDAO.searchAirports(query)
    }

    override fun getDestinationsFrom(iataCode: String): Flow<List<Airport>> {
        return airportDAO.getDestinationsFrom(iataCode)
    }

    override fun getAllFavouriteAirportsStream(): Flow<List<FavouriteAirport>> {
        return favouriteAirportDAO.getAllFavouriteAirports()
    }

    override suspend fun addFavouriteAirport(favouriteAirport: FavouriteAirport) {
        favouriteAirportDAO.insert(favouriteAirport)
    }

    override suspend fun removeFavouriteAirport(departureCode: String, destinationCode: String) {
        favouriteAirportDAO.delete(departureCode, destinationCode)
    }

    override fun getAirportByCode(code: String): Flow<Airport> {
        return airportDAO.getAirportByCode(code)
    }
}