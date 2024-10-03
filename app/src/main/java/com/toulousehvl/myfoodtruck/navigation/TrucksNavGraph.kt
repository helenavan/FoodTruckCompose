package com.toulousehvl.myfoodtruck.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.navigation.NavigationItem.Infos
import com.toulousehvl.myfoodtruck.navigation.NavigationItem.ListTrucks
import com.toulousehvl.myfoodtruck.navigation.NavigationItem.MapTruck
import com.toulousehvl.myfoodtruck.ui.theme.screens.InformationScreen
import com.toulousehvl.myfoodtruck.ui.theme.screens.MapView
import com.toulousehvl.myfoodtruck.ui.theme.screens.TrucksListScreen


@Composable
fun TrucksNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(navController, startDestination = MapTruck.route) {

        composable(
            MapTruck.route,
            arguments = listOf(navArgument("documentId") { defaultValue = "1" })
        ) { backStackEntry ->
            val truckId = backStackEntry.arguments?.getString("documentId")

            Log.d("TrucksNavGraph", "documentId ===> $truckId")

            MapView(truckId = truckId, viewModel = viewModel, navController)
        }

        composable(ListTrucks.route) {
            TrucksListScreen(navController)
        }

        composable(Infos.route) {
            InformationScreen()
        }
    }

}