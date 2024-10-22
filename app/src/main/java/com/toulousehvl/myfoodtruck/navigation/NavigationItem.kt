package com.toulousehvl.myfoodtruck.navigation


import com.toulousehvl.myfoodtruck.R
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationItem(var route: String, var title: String, var icon: Int) {
    @Serializable
    data object MapTruck :
        NavigationItem("Map".plus("/{documentId}"), "Map", R.drawable.baseline_location_on_24)

    @Serializable
    data object ListTrucks : NavigationItem("Liste", "Liste", R.drawable.baseline_list_24)

    @Serializable
    data object Infos : NavigationItem("Info", "Ajouter", R.drawable.baseline_add_24)
}