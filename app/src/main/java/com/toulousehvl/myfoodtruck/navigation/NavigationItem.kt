package com.toulousehvl.myfoodtruck.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Map
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(var route: String, val icon: ImageVector?, var title: String) {
    data object Map : NavigationItem("Map", Icons.Rounded.Map, "Map")
    data object ListTrucks : NavigationItem("Liste", Icons.Rounded.List, "Liste")
    data object Infos : NavigationItem("Info", Icons.Rounded.Info, "Info")
}