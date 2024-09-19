package com.toulousehvl.myfoodtruck.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.toulousehvl.myfoodtruck.MainViewModel
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
        Text(text = "Nom : ${truck.nameTruck}")
        truck.adresse?.let {
            //  Text(text = "Location : ${it.latitude}, ${it.longitude}")
        }
        Text(text = "Description : ${truck.categorie}")
    }
}