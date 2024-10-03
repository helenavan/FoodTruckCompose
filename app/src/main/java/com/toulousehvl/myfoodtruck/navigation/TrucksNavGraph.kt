package com.toulousehvl.myfoodtruck.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.toulousehvl.myfoodtruck.data.model.Truck
import com.toulousehvl.myfoodtruck.ui.theme.screens.InformationScreen
import com.toulousehvl.myfoodtruck.ui.theme.screens.MapView
import com.toulousehvl.myfoodtruck.ui.theme.screens.TrucksListScreen

//TODO
@Composable
fun TrucksNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.MapTruck.route.plus("/{documentId}")
    ) {
//        composable(NavigationItem.MapTruck.route) {
//            Log.d("TrucksNavGraph", "TrucksNavGraph 0 ===> ${Truck().documentId}")
//            MapView(onNavigation = {
//                navController.navigate(NavigationItem.ListTrucks.route)
//            })
//        }

        composable(
            NavigationItem.MapTruck.route.plus("/{documentId}"),
            arguments = listOf(navArgument("documentId") { defaultValue = Truck().documentId })
        ) { backStackEntry ->
            val truckId = backStackEntry.arguments?.getString("documentId")
            Log.d("Navigations", "documentId ===> $truckId")
            //   MapView()
        }

//        composable<NavigationItem.ListTrucks> {
//            TrucksListScreen(onNavigationToMap = {
//                navController.navigate(navController.navigate(NavigationItem.MapTruck.route))
//                Log.d(
//                    "TrucksNavGraph",
//                    "TrucksNavGraph 1===> ${it}"
//                )
//            })
//        }

        composable<NavigationItem.Infos> {
            InformationScreen()
        }

    }

}