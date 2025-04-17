package learningprogramming.academy.kotlinflightsearchapp

import android.app.Application
import learningprogramming.academy.kotlinflightsearchapp.data.AppContainer
import learningprogramming.academy.kotlinflightsearchapp.data.AppDataContainer

class AirportApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}