package learningprogramming.academy.kotlinflightsearchapp.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import learningprogramming.academy.kotlinflightsearchapp.SelectedAirport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import learningprogramming.academy.kotlinflightsearchapp.data.Airport
import java.io.IOException


class UserPreferenceRepository(
    private val searchQueryDataStore: DataStore<Preferences>,
    private val selectedAirportDataStore: DataStore<SelectedAirport>
) {

    private companion object {
        val SEARCH_QUERY = stringPreferencesKey("users_search_query")
        const val TAG = "UserPreferenceRepo"
    }

    suspend fun saveSearchQuery(searchQuery: String) {
        searchQueryDataStore.edit { preferences ->
            preferences[SEARCH_QUERY] = searchQuery
        }
    }

    suspend fun clearSearchQuery() {
        searchQueryDataStore.edit { preferences ->
            preferences.remove(SEARCH_QUERY)
        }
    }

    suspend fun saveSelectedAirport(airport: Airport) {
        selectedAirportDataStore.updateData { currentAirport ->
            currentAirport.toBuilder()
                .setId(airport.id.toString())
                .setName(airport.airportName)
                .setCode(airport.iataCode)
                .setPassengers(airport.passengers.toString())
                .build()
        }
    }

    val searchQuery: Flow<String> = searchQueryDataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {preferences ->
            preferences[SEARCH_QUERY] ?: ""
        }

    val selectedAirportFlow: Flow<SelectedAirport> = selectedAirportDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(SelectedAirport.getDefaultInstance())
            } else {
                throw exception
            }
        }
}

fun SelectedAirport.toAirportOrNull() : Airport? {
    val idInt = id.toIntOrNull()
    if (idInt == null) {
        return null
    }
    return Airport(
        id = idInt,
        iataCode = code,
        airportName = name,
        passengers = passengers.toInt()
    )
}
