package learningprogramming.academy.kotlinflightsearchapp.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import learningprogramming.academy.kotlinflightsearchapp.viewmodel.FlightSearchViewModel

@Composable
fun FlightSearchAppNavigation() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val flightSearchViewModel: FlightSearchViewModel =
            viewModel(factory = FlightSearchViewModel.Factory)
        val navController = rememberNavController()
        val searchBarActive = flightSearchViewModel.searchBarActive

        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                HomeScreen(
                    viewModel = flightSearchViewModel,
                    onClick = {
                        flightSearchViewModel.toggleSearchBarActive()
                        navController.navigate("search")
                    }
                )
            }
            composable("search") {
                SearchScreen(
                    viewModel = flightSearchViewModel,
                    searchBarActive = searchBarActive,
                    onBackClick = {
                        flightSearchViewModel.toggleSearchBarActive()
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}