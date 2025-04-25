package learningprogramming.academy.kotlinflightsearchapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import learningprogramming.academy.kotlinflightsearchapp.data.FavouriteAirport
import learningprogramming.academy.kotlinflightsearchapp.viewmodel.FlightSearchViewModel

@Composable
fun FavouritesList(
    viewModel: FlightSearchViewModel,
    title: String,
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
            items(favouriteAirportsList) { it ->

                val airportByDepartureCode =
                    viewModel.favouriteAirportsByDepartureCode[it.departureCode]?.collectAsState()?.value

                val airportByDestinationCode =
                    viewModel.favouriteAirportsByDestinationCode[it.destinationCode]?.collectAsState()?.value

                LaunchedEffect(it.departureCode) {
                    viewModel.getDepartureAirportByCode(it.departureCode)
                }

                LaunchedEffect(it.destinationCode) {
                    viewModel.getDestinationAirportByCode(it.destinationCode)
                }

                FlightDetailsCard(
                    viewModel = viewModel,
                    departureAirport = airportByDepartureCode,
                    destinationAirport = airportByDestinationCode,
                    isFavourite = true
                )
            }
        }
    }
}