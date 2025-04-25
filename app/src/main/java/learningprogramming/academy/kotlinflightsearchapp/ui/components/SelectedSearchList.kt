package learningprogramming.academy.kotlinflightsearchapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import learningprogramming.academy.kotlinflightsearchapp.data.Airport
import learningprogramming.academy.kotlinflightsearchapp.data.FavouriteAirport
import learningprogramming.academy.kotlinflightsearchapp.viewmodel.FlightSearchViewModel

@Composable
fun SelectedSearchList(
    viewModel: FlightSearchViewModel,
    title: String,
    selectedAirport: Airport,
    airportsList: List<Airport>,
    favouriteAirportsList: List<FavouriteAirport>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )
        LazyColumn {
            items(airportsList) {

                val isFavourite =
                    remember(favouriteAirportsList) {
                        viewModel.checkIfFavourite(selectedAirport.iataCode, it.iataCode)
                    }
                FlightDetailsCard(
                    viewModel = viewModel,
                    departureAirport = selectedAirport,
                    destinationAirport = it,
                    isFavourite = isFavourite
                )
            }
        }
    }
}