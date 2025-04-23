package learningprogramming.academy.kotlinflightsearchapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import learningprogramming.academy.kotlinflightsearchapp.data.Airport
import learningprogramming.academy.kotlinflightsearchapp.data.AirportRepository
import learningprogramming.academy.kotlinflightsearchapp.data.AirportRepositoryImpl
import learningprogramming.academy.kotlinflightsearchapp.viewmodel.FlightSearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FlightSearchViewModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val favouriteAirportList by viewModel.favouriteAirportList.collectAsState()
    val possibleDestinations by viewModel.possibleDestinations.collectAsState()
    val selectedAirport by viewModel.selectedAirport.collectAsState()

    Scaffold(
        topBar = {
            FlightSearchTopAppBar(
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 12.dp, start = 8.dp, end = 8.dp)
        ) {
            SearchBarButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            if(selectedAirport == null) {
                val favourites by viewModel.getAllFavouriteAirports().collectAsState()
                FlightsLazyColumn(
                    title = "Favourite routes",
                    selectedAirport = null,
                    items = favourites,
                    renderItem = {favourite ->
                        Text(
                            text = "${favourite.departureCode} → ${favourite.destinationCode}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    },
                    modifier = modifier
                )
            } else {
                FlightsLazyColumn(
                    title = "Flights from ${selectedAirport?.iataCode}",
                    selectedAirport = selectedAirport,
                    items = possibleDestinations,
                    renderItem = { item ->
                        FlightDetailsCard(
                            viewModel = viewModel,
                            selectedAirport = selectedAirport,
                            possibleDestination = item
                        )
                    },
                    modifier = modifier,
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
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            dividerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier.height(52.dp)
    ) {}
}

@Composable
fun <T> FlightsLazyColumn(
    title: String,
    selectedAirport: Airport?,
    items: List<T>,
    renderItem: @Composable (T) -> Unit,
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
            items(items) {item ->
                renderItem(item)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsCard(
    viewModel: FlightSearchViewModel,
    selectedAirport: Airport?,
    possibleDestination: Airport,
    modifier: Modifier = Modifier
) {
    var isFavourite by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 12.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        colors = CardColors(
            contentColor = Color.Black,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContentColor = Color.Black
        ),
        modifier = Modifier.wrapContentSize().padding(vertical = 4.dp)

    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp)
            ) {
                Text(
                    text = "DEPART",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
                if (selectedAirport != null) {
                    FlightDetailCardAirport(
                        code = selectedAirport.iataCode,
                        name = selectedAirport.airportName
                    )
                }
                Text(
                    text = "ARRIVE",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(4.dp)
                )
                FlightDetailCardAirport(
                    code = possibleDestination.iataCode,
                    name = possibleDestination.airportName
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = if (isFavourite) Color.Red else Color.DarkGray,
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

@Composable
fun FlightDetailCardAirport(
    code: String,
    name: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .padding(4.dp)
    ) {
        Text(
            text = code,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = name,
            fontSize = 14.sp,
            overflow = TextOverflow.Clip,
            maxLines = 1,
            color = Color.DarkGray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "Flight Search"
            )
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

//@Preview(showSystemUi = true)
//@Composable
//fun HomeScreenPreview() {
//    Scaffold(
//        modifier = Modifier.statusBarsPadding()
//    ) {
//        Surface(
//            modifier = Modifier
//                .padding(it)
//        ) {
//            HomeScreen(
//                viewModel = fakeViewModel,
//                onClick = {}
//            )
//        }
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun FlightDetailsCardPreview() {
//    FlightDetailsCard(
//        selectedAirport = Airport(1,"OPO", "Francisco Sá Carneiro Airport", 5053134),
//        possibleDestination = Airport(7,"SVO", "Sheremetyevo - A.S. Pushkin international airport", 49933000)
//    )
//}