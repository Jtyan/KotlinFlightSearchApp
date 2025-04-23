package learningprogramming.academy.kotlinflightsearchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import learningprogramming.academy.kotlinflightsearchapp.AirportApplication
import learningprogramming.academy.kotlinflightsearchapp.data.Airport
import learningprogramming.academy.kotlinflightsearchapp.data.AirportRepositoryImpl
import learningprogramming.academy.kotlinflightsearchapp.data.FavouriteAirport

class FlightSearchViewModel(
    private val airportRepository: AirportRepositoryImpl
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedAirport = MutableStateFlow<Airport?>(null)
    val selectedAirport = _selectedAirport.asStateFlow()

    private val _favouriteAirportList = MutableStateFlow<List<FavouriteAirport>>(emptyList())
    val favouriteAirportList = _favouriteAirportList.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val possibleDestinations: StateFlow<List<Airport>> =
        selectedAirport.flatMapLatest { airport ->
            airport?.let {
                airportRepository.getDestinationsFrom(it.iataCode)
            } ?: flowOf(emptyList())
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val autocompleteSuggestions: StateFlow<List<Airport>> =
        searchQuery.flatMapLatest { query ->
            airportRepository.searchAirportsStream(query)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectAirport(airport: Airport) {
        _selectedAirport.value = airport
    }

    fun clearSelectedAirport() {
        _selectedAirport.value = null
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AirportApplication)
                val airportRepository = application.container.airportRepository
                FlightSearchViewModel(airportRepository)
            }
        }
    }
}