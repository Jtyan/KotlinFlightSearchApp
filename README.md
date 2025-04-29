# FlightSearch App (With Room and DataStore)


## Overview

This is an Android application that allows users to search for flights and their destinations in the local database and save them as favourites.

### Demo Video
[Demo Video](https://github.com/user-attachments/assets/e2b56bff-19d5-4304-8715-51bad82839ac)

### Screenshots
![HomeScreen](https://github.com/user-attachments/assets/fb6c0411-87dc-466e-a29b-69478e9c75ed) &nbsp;
![HomeScreenFavouriteRoutes](https://github.com/user-attachments/assets/b2feb075-627f-4934-b118-dc13656c92a2) &nbsp;
![SearchScreen](https://github.com/user-attachments/assets/4cfb59b4-344d-4149-b8ff-308f6c4d0313)


## Install and Run

1. Clone the repository:
````
git clone https://github.com/Jtyan/KotlinFlightSearchApp.git
````
2. Open the project in Android Studio.
   
3. Build and run the project on an emulator or your android device.

## Features

* Instant book searches with autocomplete suggestions
* Ability to save routes as favourites
* Modern and clean layout displaying different routes
* Preloaded SQLite database with airport and favourite tables 

## Tech Stack

- **Kotlin** as the main programming language
- **Room** for local SQLite database access with KSP for annotation processing
- **Jetpack Compose** for a fully declarative UI
- **Jetpack Navigation Component** for screen transitions
- **Preferences DataStore** to save search queries
- **Proto DataStore** for storing selected airport
- **MVVM Architecture** for clean separation of concerns
- **ViewModel** to manage all UI-related state
