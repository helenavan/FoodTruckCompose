package com.toulousehvl.myfoodtruck.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.data.CategoryTruck
import com.toulousehvl.myfoodtruck.data.CategoryTruck.Companion.toCategoryTruckString
import com.toulousehvl.myfoodtruck.data.Truck

@Composable
fun TrucksListScreen(viewModel: MainViewModel = viewModel()) {

    val dataList by viewModel.dataState.collectAsStateWithLifecycle()

    Log.d("TrucksListScreen", "Data list ===> $dataList")


    Column(modifier = Modifier.fillMaxSize()) {
        TruckList(trucks = dataList)
    }
}

@Composable
fun TruckList(trucks: List<Truck>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items =trucks) { truck ->
            TruckItem(truck = truck)
        }
    }
}

@Composable
fun TruckItem(truck: Truck) {
    Column(modifier = Modifier.padding(8.dp)) {
        Image(
            painter = painterResource(id = setTruckCategorie(truck.categorie.toString())),
            contentDescription = "frittes"
        )
        Text(text = "Nom : ${truck.nameTruck}")
        truck.adresse?.let {
            //  Text(text = "Location : ${it.latitude}, ${it.longitude}")
        }
        Text(text = "Description : ${truck.categorie}")
    }
}

private fun setTruckCategorie(category: String): Int {
    return when (category) {
        CategoryTruck.Italian.toCategoryTruckString() -> R.drawable.ic_frittes
        CategoryTruck.Burger.toCategoryTruckString() -> R.drawable.ic_burger
        CategoryTruck.Asian.toCategoryTruckString() -> R.drawable.ic_thai
        CategoryTruck.Sushi.toCategoryTruckString() -> R.drawable.ic_sushi
        CategoryTruck.African.toCategoryTruckString() -> R.drawable.ic_africain
        CategoryTruck.Kebab.toCategoryTruckString() -> R.drawable.ic_kebab
        else -> R.drawable.ic_frittes
    }
}