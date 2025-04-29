package learningprogramming.academy.kotlinflightsearchapp.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import learningprogramming.academy.kotlinflightsearchapp.ui.components.SelectedSearchList
import learningprogramming.academy.kotlinflightsearchapp.ui.components.FavouritesList
import learningprogramming.academy.kotlinflightsearchapp.viewmodel.FlightSearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FlightSearchViewModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedAirport = viewModel.selectedAirport.collectAsState().value
    val possibleDestinationsList by viewModel.possibleDestinations.collectAsState()
    val favouriteAirportList by viewModel.favouriteAirportList.collectAsState()

    Scaffold(
        topBar = {
            FlightSearchTopAppBar()
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchBarButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if (selectedAirport != null) {
                Log.d("Column 1", "selectedAirport = $selectedAirport")
                SelectedSearchList(
                    viewModel = viewModel,
                    title = "Flights from ${selectedAirport.iataCode}",
                    favouriteAirportsList = favouriteAirportList,
                    selectedAirport = selectedAirport,
                    airportsList = possibleDestinationsList
                )
            } else {
                FavouritesList(
                    viewModel = viewModel,
                    title = "Favourite routes",
                    favouriteAirportsList = favouriteAirportList
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val inputField = @Composable {
        InputField(
            query = "",
            onQueryChange = {},
            onSearch = {
            },
            expanded = false,
            onExpandedChange = { if (it) onClick() },
            modifier = modifier,
            placeholder = { Text("Enter departure airport") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
        )
    }
    SearchBar(
        inputField = inputField,
        expanded = false,
        onExpandedChange = {},
        colors = SearchBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            dividerColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopAppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Flight Search"
            )
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}
