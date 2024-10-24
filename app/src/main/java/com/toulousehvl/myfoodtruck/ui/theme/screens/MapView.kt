package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.content.Context
import android.graphics.drawable.Drawable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.data.model.Truck
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
    viewModel: TrucksListViewModel = hiltViewModel()
) {
   // val selectedTruck by viewModel.selectedTruckState
    val listOfTrucks by viewModel.dataListTrucksState.collectAsStateWithLifecycle()
    Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME
    val mapView = rememberMapViewWithLifecycle()
    val mapController = mapView.controller

    mapController.setZoom(15.0)
    mapView.setTileSource(TileSourceFactory.MAPNIK)
    mapView.setMultiTouchControls(true)

    val mLocationOverlay = rememberUserLocationOverlay(mapView)

    var startPoint: GeoPoint? = null

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(factory = { mapView }) { view ->

            addTruckMarkersToMap(mapView, listOfTrucks)
            //TODO when user clicks on item of list, move camera to marker
//            //center la carte sur l'utilisateur
//            mLocationOverlay?.myLocation?.let { userLocation ->
//                startPoint = GeoPoint(userLocation.latitude, userLocation.longitude)
//                mapController?.animateTo(
//                    GeoPoint(userLocation.latitude, userLocation.longitude),
//                    15.5,
//                    1
//                )
//            }
            view.invalidate() //rafraîchir la carte

        }

        FloatingActionButton(
            onClick = { centerMapOnUserLocation(mLocationOverlay, mapView) },
            containerColor = YellowBanane,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.MyLocation, "Add", tint = Color.Black)
        }
    }
}

@Composable
fun rememberUserLocationOverlay(mapView: org.osmdroid.views.MapView): MyLocationNewOverlay {
    val context = LocalContext.current
    val locationProvider = GpsMyLocationProvider(context)
    locationProvider.locationUpdateMinTime = 1000L
    val mapController = mapView.controller

    return remember {
        MyLocationNewOverlay(locationProvider, mapView).apply {
            enableMyLocation()
            enableFollowLocation()
            myLocation?.let {
                val geoPoint = GeoPoint(it.latitude, it.longitude)
                mapController?.animateTo(geoPoint, 15.5, 1)
            }
            mapView.overlays.add(this)
        }
    }
}

fun addTruckMarkersToMap(mapView: org.osmdroid.views.MapView, trucks: List<Truck>) {
    for (truck in trucks) {
        truck.latd?.let { lat ->
            truck.lgtd?.let { lng ->
                val truckGeoPoint = GeoPoint(lat, lng)
                val truckMarker = Marker(mapView).apply {
                    icon = getMarkerIcon(mapView.context, truck.categorie ?: "Burger")
                    position = truckGeoPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = truck.nameTruck
                    snippet = truck.categorie
                    subDescription = truck.adresse
                }
                mapView.overlays.add(truckMarker)
            }
        }
    }
}

fun getMarkerIcon(context: Context, categorie: String): Drawable? {
    return when (categorie) {
        "Italien/Pizza" -> getDrawable(context, R.drawable.ic_marqueur_italien)
        "Asiatique" -> getDrawable(context, R.drawable.ic_marqueur_asia)
        "Africain" -> getDrawable(context, R.drawable.ic_marqueur_africain)
        "Kebab" -> getDrawable(context, R.drawable.ic_marqueur_kebab)
        "Japonais" -> getDrawable(context, R.drawable.ic_marqueur_japonais)
        "Burger" -> getDrawable(context, R.drawable.ic_marqueur_burger)
        else -> getDrawable(context, R.drawable.ic_marqueur_burger)
    }
}

fun centerMapOnUserLocation(
    mLocationOverlay: MyLocationNewOverlay?,
    mapView: org.osmdroid.views.MapView
) {
    mLocationOverlay?.myLocation?.let { userLocation ->
        val geoPoint = GeoPoint(userLocation.latitude, userLocation.longitude)
        mapView.controller?.animateTo(geoPoint, 15.5, 1)
    }
}
