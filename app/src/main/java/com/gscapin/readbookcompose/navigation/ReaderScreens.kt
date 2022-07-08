package com.gscapin.readbookcompose.navigation

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    CreateAccountScreen,
    HomeScreen,
    SearchScreen,
    DetailsScreen,
    StatsScreen,
    UpdateScreen;

    companion object {
        fun fromRoute(route: String): ReaderScreens =
            when (route?.substringBefore("/")) {
                SplashScreen.name -> SplashScreen
                LoginScreen.name -> LoginScreen
                CreateAccountScreen.name -> CreateAccountScreen
                HomeScreen.name -> HomeScreen
                SearchScreen.name -> SearchScreen
                DetailsScreen.name -> DetailsScreen
                StatsScreen.name -> StatsScreen
                UpdateScreen.name -> UpdateScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")

            }
    }
}