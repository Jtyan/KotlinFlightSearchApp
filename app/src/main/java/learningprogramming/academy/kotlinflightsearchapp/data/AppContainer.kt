package learningprogramming.academy.kotlinflightsearchapp.data

import android.content.Context

interface AppContainer {
    val airportRepository: AirportRepositoryImpl
}

class AppDataContainer(
    private val context: Context
): AppContainer {
    override val airportRepository: AirportRepositoryImpl by lazy  {
        AirportRepositoryImpl(
            FlightSearchDatabase.getDatabase(context).airportDAO(),
            FlightSearchDatabase.getDatabase(context).favouriteAirportDAO()
        )
    }
}