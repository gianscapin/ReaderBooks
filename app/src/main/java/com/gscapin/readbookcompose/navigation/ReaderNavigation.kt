package com.gscapin.readbookcompose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gscapin.readbookcompose.screens.SplashScreen
import com.gscapin.readbookcompose.screens.home.Home
import com.gscapin.readbookcompose.screens.login.LoginScreen
import com.gscapin.readbookcompose.screens.stats.StatsScreen

@Composable
fun ReaderNavigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){
        composable(ReaderScreens.SplashScreen.name){
            SplashScreen(navController)
        }

        composable(ReaderScreens.HomeScreen.name){
            Home(navController)
        }

        composable(ReaderScreens.LoginScreen.name){
            LoginScreen(navController)
        }

        composable(ReaderScreens.StatsScreen.name){
            StatsScreen(navController)
        }

    }
}