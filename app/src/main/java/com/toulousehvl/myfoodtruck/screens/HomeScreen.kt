package com.toulousehvl.myfoodtruck.screens

import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.toulousehvl.myfoodtruck.CenterText
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import kotlin.coroutines.Continuation

@Composable
fun HomeScreen(context: Context) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                val mapView = MapView(context)
                mapView
            },
            modifier = Modifier.fillMaxSize()
        ) { mapView ->
            Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            val mapController = mapView.controller
            mapController.setZoom(9.5)
            val startPoint = GeoPoint(32.627387981943, 51.62582233021288)
            mapController.setCenter(startPoint)
        }
    }
}