package learningprogramming.academy.kotlinflightsearchapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import learningprogramming.academy.kotlinflightsearchapp.data.Airport
import learningprogramming.academy.kotlinflightsearchapp.viewmodel.FlightSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsCard(
    viewModel: FlightSearchViewModel,
    departureAirport: Airport?,
    destinationAirport: Airport?,
    isFavourite: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 12.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        colors = CardColors(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContentColor = Color.Black
        ),
        modifier = Modifier
            .wrapContentSize()
            .padding(vertical = 4.dp)

    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp)
            ) {
                Text(
                    text = "DEPART",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
                FlightCodeAndName(
                    code = departureAirport?.iataCode ?: "",
                    name = departureAirport?.airportName ?: ""
                )
                Text(
                    text = "ARRIVE",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
                FlightCodeAndName(
                    code = destinationAirport?.iataCode ?: "",
                    name = destinationAirport?.airportName ?: ""
                )
            }
            IconButton(
                onClick = {
                    if (!isFavourite) {
                        viewModel.addFavourite(
                            departureCode = departureAirport?.iataCode ?: "",
                            destinationCode = destinationAirport?.iataCode ?: ""
                        )
                    } else {
                        viewModel.removeFavourite(
                            departureCode = departureAirport?.iataCode ?: "",
                            destinationCode = destinationAirport?.iataCode ?: ""
                        )
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint =
                        if (isFavourite)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceBright,
                    contentDescription = "favourite",
                    modifier = Modifier
                        .weight(1f)
                        .size(28.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}