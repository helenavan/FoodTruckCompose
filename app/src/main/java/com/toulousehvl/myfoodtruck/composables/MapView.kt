package com.toulousehvl.myfoodtruck.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.toulousehvl.myfoodtruck.MainViewModel
import org.osmdroid.views.MapView

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onLoad: ((map: MapView) -> Unit)? = null,
    viewModel: MainViewModel
) {
    val mapView = rememberMapViewWithLifecycle()
   // val uiState by viewModel.userLocation.collectAsStateWithLifecycle()

    AndroidView(
        factory = { contextMap ->
            viewModel.createMap(contextMap, mapView)
            mapView
        },
        modifier = modifier.fillMaxSize()
    ) {
        // Callback une fois que la carte est prête
        onLoad?.invoke(mapView)
        }
}