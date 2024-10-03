package com.toulousehvl.myfoodtruck.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.toulousehvl.myfoodtruck.navigation.NavigationItem.Infos
import com.toulousehvl.myfoodtruck.navigation.NavigationItem.ListTrucks
import com.toulousehvl.myfoodtruck.navigation.NavigationItem.MapTruck
import com.toulousehvl.myfoodtruck.ui.theme.theme.YellowBanane
import com.toulousehvl.myfoodtruck.ui.theme.theme.YellowLite

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val items = listOf(
        MapTruck,
        ListTrucks,
        Infos,
    )

    NavigationBar(containerColor = YellowBanane) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                modifier = Modifier.background(color = YellowBanane),
                alwaysShowLabel = true,
                icon = {
                    //TODO : Set icon
                    //Icon(item.icon!!, contentDescription = item.title, tint = Color.Black)
                },
                label = { Text(item.title, color = Color.Gray) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = YellowLite
                ),
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                inclusive = false
                                saveState = false
                            }
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }
    }
}