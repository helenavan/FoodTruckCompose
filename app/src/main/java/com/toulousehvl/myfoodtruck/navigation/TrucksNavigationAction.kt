package com.toulousehvl.myfoodtruck.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object TrucksDestinations {
    const val MAP_ROUTE = "Map"
    const val TRUCKS_LIST_ROUTE = "Liste"
    const val INFOS_ROUTE = "Infos"
}

class TrucksNavigationAction(navController: NavHostController) {
    val navigateToTrucksList: () -> Unit = {
        navController.navigate(TrucksDestinations.TRUCKS_LIST_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = false
                inclusive = false
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = false
        }
    }
    val navigateToInfos: () -> Unit = {
        navController.navigate(TrucksDestinations.INFOS_ROUTE) {
            navController.navigate(TrucksDestinations.INFOS_ROUTE) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    val navigateToMap: () -> Unit = {
        navController.navigate(TrucksDestinations.MAP_ROUTE) {
            navController.navigate(TrucksDestinations.MAP_ROUTE) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}