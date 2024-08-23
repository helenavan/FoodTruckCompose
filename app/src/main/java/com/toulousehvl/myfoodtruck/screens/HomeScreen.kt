package com.toulousehvl.myfoodtruck.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.composables.MapView

@Composable
fun HomeScreen(viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        MapView(viewModel = viewModel)
    }
}