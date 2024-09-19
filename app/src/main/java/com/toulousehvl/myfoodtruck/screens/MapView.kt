package com.toulousehvl.myfoodtruck.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.composables.rememberMapViewWithLifecycle
import com.toulousehvl.myfoodtruck.ui.theme.YellowBanane
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapView(
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var mLocationOverlay: MyLocationNewOverlay? = null
    val mapView = rememberMapViewWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME
        // val uiState by viewModel.userLocation.collectAsStateWithLifecycle()

        AndroidView({ mapView }) { view ->
            // onLoad?.invoke(view)

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

            // Handle marker click events
//       marker.setOnMarkerClickListener { _, _ ->
//           Toast.makeText(view.context, "Marker clicked!", Toast.LENGTH_SHORT).show()
//           true
//       }

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
