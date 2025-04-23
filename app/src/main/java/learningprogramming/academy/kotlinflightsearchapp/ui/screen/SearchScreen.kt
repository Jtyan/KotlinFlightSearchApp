package learningprogramming.academy.kotlinflightsearchapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import learningprogramming.academy.kotlinflightsearchapp.data.Airport
import learningprogramming.academy.kotlinflightsearchapp.viewmodel.FlightSearchViewModel

@Composable
fun SearchScreen(
    viewModel: FlightSearchViewModel,
    searchBarActive: Boolean,
    onBackClick: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val autoCompleteSuggestions by viewModel.autocompleteSuggestions.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FlightSearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = {
                if (searchQuery.isEmpty()) {
                    viewModel.clearSelectedAirport()
                    onBackClick()
                }
            },
            onBackClick = onBackClick,
            active = searchBarActive,
            onExpandedChange = { searchQuery != "" },
            autoCompleteSuggestions = autoCompleteSuggestions,
            onAirportClick = { viewModel.selectAirport(it) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onBackClick: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    autoCompleteSuggestions: List<Airport>,
    onAirportClick: (Airport) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(active) {
        focusRequester.requestFocus()
    }

    val inputField = @Composable {
        InputField(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            expanded = active,
            onExpandedChange = onExpandedChange,
            placeholder = { Text("Enter departure airport") },
            leadingIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { onQueryChange("") }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            }
        )
    }
    SearchBar(
        inputField = inputField,
        expanded = active,
        onExpandedChange = onExpandedChange,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    ) {
        if (query.isNotEmpty()) {
            LazyColumn {
                items(autoCompleteSuggestions) {
                    ListItem(
                        headlineContent = {
                            FlightDetailCardAirport(
                                code = it.iataCode,
                                name = it.airportName
                            )
                        },
                        modifier = Modifier
                            .clickable {
                                onAirportClick(it)
                                onBackClick()
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
