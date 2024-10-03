package com.toulousehvl.myfoodtruck.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationItem(var route: String, var title: String) {
    @Serializable
    data object MapTruck : NavigationItem("Map", "Map")

    @Serializable
    data object ListTrucks : NavigationItem("Liste", "Liste")

    @Serializable
    data object Infos : NavigationItem("Info", "Info")
}