package com.gscapin.readbookcompose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gscapin.readbookcompose.screens.SplashScreen
import com.gscapin.readbookcompose.screens.details.BookDetailsScreen
import com.gscapin.readbookcompose.screens.home.Home
import com.gscapin.readbookcompose.screens.login.LoginScreen
import com.gscapin.readbookcompose.screens.search.BookSearchViewModel
import com.gscapin.readbookcompose.screens.search.SearchScreen
import com.gscapin.readbookcompose.screens.stats.StatsScreen

@Composable
fun ReaderNavigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {
        composable(ReaderScreens.SplashScreen.name) {
            SplashScreen(navController)
        }

        composable(ReaderScreens.HomeScreen.name) {
            Home(navController)
        }

        composable(ReaderScreens.LoginScreen.name) {
            LoginScreen(navController)
        }

        composable(ReaderScreens.StatsScreen.name) {
            StatsScreen(navController)
        }

        composable(ReaderScreens.SearchScreen.name) {
            val viewModel = hiltViewModel<BookSearchViewModel>()
            SearchScreen(navController, viewModel)
        }

        val detailName = ReaderScreens.BookDetailsScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId")
                ?.let { BookDetailsScreen(navController, bookId = it.toString()) }
        }

    }
}