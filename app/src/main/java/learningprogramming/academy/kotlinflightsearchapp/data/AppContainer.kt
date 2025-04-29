package learningprogramming.academy.kotlinflightsearchapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import learningprogramming.academy.kotlinflightsearchapp.SelectedAirport
import learningprogramming.academy.kotlinflightsearchapp.data.local.SelectedAirportSerializer
import learningprogramming.academy.kotlinflightsearchapp.data.local.UserPreferenceRepository

interface AppContainer {
    val airportRepository: AirportRepositoryImpl
    val userPreferenceRepository: UserPreferenceRepository
}

private const val SEARCH_QUERY_PREFERENCE = "search_query_preference"

class AppDataContainer(
    private val context: Context
): AppContainer {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = SEARCH_QUERY_PREFERENCE
    )

    private val Context.selectedAirportDataStore: DataStore<SelectedAirport> by dataStore(
        fileName = "selected_airport.pb",
        serializer = SelectedAirportSerializer
    )

    override val airportRepository: AirportRepositoryImpl by lazy  {
        AirportRepositoryImpl(
            FlightSearchDatabase.getDatabase(context).airportDAO(),
            FlightSearchDatabase.getDatabase(context).favouriteAirportDAO()
        )
    }
    override val userPreferenceRepository: UserPreferenceRepository by lazy {
        UserPreferenceRepository(
            searchQueryDataStore = context.dataStore,
            selectedAirportDataStore = context.selectedAirportDataStore)
    }

}