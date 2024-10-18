package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import androidx.appcompat.content.res.AppCompatResources.getDrawable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.R
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
fun MapView(
    truckId: String? = "null",
    viewModel: MainViewModel = hiltViewModel()
) {
   // val selectedTruck by viewModel.selectedTruckState
    val listOfTrucks by viewModel.dataListTrucksState.collectAsStateWithLifecycle()

    var mLocationOverlay: MyLocationNewOverlay? = null
    val mapView = rememberMapViewWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {

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

            for (truck in listOfTrucks) {
                val truckGeoPoint = GeoPoint(truck.latd!!, truck.lgtd!!)
                val truckMarker = Marker(mapView)
                //custom marker
                truckMarker.icon = when (truck.categorie) {
                    "Italien/Pizza" -> getDrawable(view.context, R.drawable.ic_marqueur_italien)
                    "Thaï" -> getDrawable(view.context, R.drawable.ic_marqueur_thai)
                    "Sushi" -> getDrawable(view.context, R.drawable.ic_marqueur_asia)
                    "African" -> getDrawable(view.context, R.drawable.ic_marqueur_africain)
                    "Kebab" -> getDrawable(view.context, R.drawable.ic_marqueur_kebab)
                    "Japonais" -> getDrawable(view.context, R.drawable.ic_marqueur_japonais)
                    else -> getDrawable(view.context, R.drawable.ic_marqueur_burger)
                }

                truckMarker.position = truckGeoPoint
                truckMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                truckMarker.title = truck.nameTruck
                truckMarker.snippet = truck.categorie

                mapView.overlays.add(truckMarker)
            }

            //TODO zoom sur le marqueur selectionne via la liste
//            val selectedTruckInList = listOfTrucks.find { it.documentId == truckId }
//            mapController.animateTo(selectedTruckInList?.let {GeoPoint(it.latd!!, it.lgtd!!)}, 16.5, 5)

            view.overlays.add(mLocationOverlay)
            Log.d("MapView", "selectedTruck ===> $truckId")


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
