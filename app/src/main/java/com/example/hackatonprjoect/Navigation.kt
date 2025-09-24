package com.example.hackatonprjoect

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.hackatonprjoect.common.utils.AppObserver
import com.example.hackatonprjoect.presentation.home.HomeScreen
import com.example.hackatonprjoect.presentation.home.HomeViewModel
import com.example.hackatonprjoect.presentation.main.MainScreen
import com.example.hackatonprjoect.presentation.treauser_hunt.FindArtPlaces

object NavigationRoutes {
    const val MAIN = "main"



    const val HOME = "home"
    const val MAP = "map"
    const val SEARCH = "search"
    const val FIND_ART_PIECES = "At The Airport"
    const val TRAVELLER_GUIDE = "Traveller Guide"

}

@Composable
fun Navigation(
    navController: NavHostController,
    appObserver: AppObserver,
    homeViewModel: HomeViewModel,
    paddingValues: PaddingValues,
    onEnterPlayZoneClick: () -> Unit
) {
//    val navController = rememberNavController()


    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController, startDestination = NavigationRoutes.MAIN, modifier = Modifier
    ) {

        composable(NavigationRoutes.MAIN) {
            MainScreen(appObserver) {
                onEnterPlayZoneClick()
            }
        }

        composable(NavigationRoutes.HOME) {
            HomeScreen(navController, homeViewModel)
        }


        composable(NavigationRoutes.FIND_ART_PIECES) {
            FindArtPlaces(appObserver)
        }
    }
}