package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.ui.theme.composables.rememberMapViewWithLifecycle
import com.toulousehvl.myfoodtruck.ui.theme.theme.YellowBanane
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapView(truckId: String? = "null", viewModel: MainViewModel = hiltViewModel(), navHostController: NavHostController) {
    val selectedTruck by viewModel.selectedTruckState

    var mLocationOverlay: MyLocationNewOverlay? = null
    val mapView = rememberMapViewWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Log.d(("MapView"), "truckId selected ===> $truckId + ${selectedTruck?.nameTruck}")

        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME

        AndroidView({ mapView }) { view ->

            view.setTileSource(TileSourceFactory.MAPNIK)
            view.setMultiTouchControls(true)

            // Initialize the MapView
            val mapController = view.controller

            var startPoint = GeoPoint(48.8583, 2.2944)

            val locationProvider = GpsMyLocationProvider(view.context)

            mLocationOverlay = MyLocationNewOverlay(locationProvider, view).apply {
                enableMyLocation()
                enableFollowLocation()
                myLocation?.let {
                    startPoint = GeoPoint(it.latitude, it.longitude)
                }
            }
            mapController.animateTo(startPoint, 16.5, 5)
            view.overlays.add(mLocationOverlay)

            val marker = Marker(mapView)
            marker.title = "Info Marker"
            mapView.overlays.add(marker)

            // Mettre à jour le contenu du marqueur lorsque l'utilisateur sélectionne un GeoPoint
//            selectedGeoPoint?.let {
//                marker.position = it
//                marker.snippet = "Latitude: ${it.latitude}, Longitude: ${it.longitude}"
//                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
//                marker.showInfoWindow()
//            }

            // Handle marker click events
            marker.setOnMarkerClickListener { _, _ ->
                Toast.makeText(view.context, "Marker clicked!", Toast.LENGTH_SHORT).show()
                true
            }

            // Refresh the map
            view.invalidate()
        }

        FloatingActionButton(
            onClick = {
                mLocationOverlay?.myLocation?.let { userLocation ->
                    val map = mLocationOverlay?.mMyLocationProvider?.lastKnownLocation
                    mapView.controller?.animateTo(map?.latitude?.let {
                        GeoPoint(
                            it,
                            map.longitude
                        )
                    })
                }
            },
            containerColor = YellowBanane,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.MyLocation, "Add", tint = Color.Black)
        }
    }
}
