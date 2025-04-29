package learningprogramming.academy.kotlinflightsearchapp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import learningprogramming.academy.kotlinflightsearchapp.AirportApplication
import learningprogramming.academy.kotlinflightsearchapp.data.Airport
import learningprogramming.academy.kotlinflightsearchapp.data.AirportRepositoryImpl
import learningprogramming.academy.kotlinflightsearchapp.data.FavouriteAirport
import learningprogramming.academy.kotlinflightsearchapp.data.local.UserPreferenceRepository
import learningprogramming.academy.kotlinflightsearchapp.data.local.toAirportOrNull

class FlightSearchViewModel(
    private val airportRepository: AirportRepositoryImpl,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    var searchBarActive by mutableStateOf(false)
        private set

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedAirport = MutableStateFlow<Airport?>(null)
    val selectedAirport = _selectedAirport.asStateFlow()

    private val _favouriteAirportsByDepartureCode =
        mutableStateMapOf<String, StateFlow<Airport?>>()
    val favouriteAirportsByDepartureCode: SnapshotStateMap<String, StateFlow<Airport?>> =
        _favouriteAirportsByDepartureCode

    private val _favouriteAirportsByDestinationCode =
        mutableStateMapOf<String, StateFlow<Airport?>>()
    val favouriteAirportsByDestinationCode: SnapshotStateMap<String, StateFlow<Airport?>> =
        _favouriteAirportsByDestinationCode

    @OptIn(ExperimentalCoroutinesApi::class)
    val possibleDestinations: StateFlow<List<Airport>> =
        _selectedAirport.flatMapLatest { airport ->
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

    init {
        viewModelScope.launch {
            combine(
                userPreferenceRepository.searchQuery,
                userPreferenceRepository.selectedAirportFlow
            ) { savedQuery, savedSelectedAirport ->
                savedQuery to savedSelectedAirport
            }.collect { (savedQuery, savedSelectedAirport) ->
                _searchQuery.value = savedQuery
                _selectedAirport.value =
                    savedSelectedAirport.toAirportOrNull()
            }
        }
    }

    fun toggleSearchBarActive() {
        searchBarActive = !searchBarActive
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            userPreferenceRepository.saveSearchQuery(query)
        }
    }

    fun selectAirport(airport: Airport) {
        _selectedAirport.value = airport
        viewModelScope.launch {
            userPreferenceRepository.saveSelectedAirport(airport)
        }
    }

    fun clearSelectedAirport() {
        _selectedAirport.value = null
    }

    val favouriteAirportList: StateFlow<List<FavouriteAirport>> =
        airportRepository.getAllFavouriteAirportsStream()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = emptyList()
            )

    fun getDepartureAirportByCode(code: String) {
        Log.d("GetAirportByCode", "getAirportCode $code")
        if (_favouriteAirportsByDepartureCode[code] != null) return
        val airport = airportRepository.getAirportByCode(code)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = null
            )
        _favouriteAirportsByDepartureCode[code] = airport
    }

    fun getDestinationAirportByCode(code: String) {
        Log.d("GetAirportByCode", "getAirportCode $code")
        if (_favouriteAirportsByDestinationCode[code] != null) return
        val airport = airportRepository.getAirportByCode(code)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = null
            )
        _favouriteAirportsByDestinationCode[code] = airport
    }

    fun addFavourite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            val favouriteAirport = FavouriteAirport(
                id = 0,
                departureCode = departureCode,
                destinationCode = destinationCode,
            )
            airportRepository.addFavouriteAirport(favouriteAirport)
            Log.d("addFavourite", "AddingFavouriteAirport")
        }
    }

    fun removeFavourite(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            airportRepository.removeFavouriteAirport(departureCode, destinationCode)
        }
    }

    fun checkIfFavourite(departureCode: String, destinationCode: String): Boolean {
        Log.d("CheckIfFavourite", "check $departureCode, $destinationCode")
        return favouriteAirportList.value.any { fav ->
            fav.departureCode == departureCode &&
                    fav.destinationCode == destinationCode
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AirportApplication)
                val airportRepository = application.container.airportRepository
                val userPreferenceRepository = application.container.userPreferenceRepository
                FlightSearchViewModel(
                    airportRepository = airportRepository,
                    userPreferenceRepository = userPreferenceRepository
                )
            }
        }
    }
}



