package learningprogramming.academy.kotlinflightsearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import learningprogramming.academy.kotlinflightsearchapp.ui.screen.FlightSearchAppNavigation
import learningprogramming.academy.kotlinflightsearchapp.ui.theme.KotlinFlightSearchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinFlightSearchAppTheme {
                FlightSearchAppNavigation()
            }
        }
    }
}

