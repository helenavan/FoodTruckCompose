package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.data.model.Truck
import com.toulousehvl.myfoodtruck.data.utils.MapsUtils.Companion.getAddressFromGeoPoint
import com.toulousehvl.myfoodtruck.ui.theme.composables.InputDialog
import com.toulousehvl.myfoodtruck.ui.theme.composables.rememberMapViewWithLifecycle
import com.toulousehvl.myfoodtruck.ui.theme.theme.YellowBanane
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapView(
    truckId: String? = "null",
    viewModel: TrucksListViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    viewModel.fetchDataFromFirestore()
    val listOfTrucks by viewModel.dataListTrucksState.collectAsStateWithLifecycle()

    val mapView = rememberMapViewWithLifecycle()
    val mapController = mapView.controller

    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val foodTruckName = viewModel.foodTruckName
    val foodTruckAddress = viewModel.foodTruckAddress
    val foodTruckCategory = viewModel.selectedCategory
    val showErrorField = viewModel.showError

    Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME

    mapController.zoomTo(16.0)
    mapView.zoomController.setVisibility(org.osmdroid.views.CustomZoomButtonsController.Visibility.NEVER)
    mapView.setTileSource(TileSourceFactory.MAPNIK)
    mapView.setMultiTouchControls(true)

    val mLocationOverlay = rememberUserLocationOverlay(mapView, viewModel)

    //TODO center foodtruck
//    LaunchedEffect(key1 = truckId) {
    // mLocationOverlay.enableMyLocation()
//        mLocationOverlay.enableFollowLocation()
//    }

    // centerMapOnTruckLocation(truckId, mapView, viewModel)

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(factory = { mapView }) { view ->

        addTruckMarkersToMap(mapView, listOfTrucks)

            view.invalidate() //rafraîchir la carte

        }

        if (showDialog && selectedLocation != null) {

            getAddressFromGeoPoint(LocalContext.current, GeoPoint(selectedLocation))?.let {

                    addressFT ->
                viewModel.onFoodTruckAddressChange(addressFT)

                InputDialog(
                    dialogTitle = stringResource(R.string.add_one_food_truck),
                    address = foodTruckAddress,
                    nameTruck = foodTruckName,
                    category = foodTruckCategory,
                    onTextAddressChange = viewModel::onFoodTruckAddressChange,
                    onCategorySelected = viewModel::onCategorySelected,
                    onTextNameChange = { newValue -> viewModel.onFoodTruckNameChange(newValue) },
                    onConfirm = {
                        viewModel.addFoodTruckToFirestore(context)
                        showDialog = viewModel.showError
                    },
                    onDismiss = { showDialog = false },
                    show = showDialog,
                    categories = LocalContext.current.resources.getStringArray(R.array.food_categories)
                        .toList(),
                    showError = showErrorField,
                    onShowErrorChange = { viewModel.onShowError(it) }
                )
            }
        }

        val selectTruckOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }

            override fun longPressHelper(geoPoint: GeoPoint?): Boolean {
                selectedLocation = geoPoint
                showDialog = true
                return true
            }

        })
        mapView.overlays.add(selectTruckOverlay)

        FloatingActionButton(
            onClick = {
                centerMapOnUserLocation(
                    mLocationOverlay,
                    mapView,
                    viewModel::onUserLocationChange
                )
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

@Composable
fun rememberUserLocationOverlay(
    mapView: MapView,
    viewModel: TrucksListViewModel
): MyLocationNewOverlay {
    val context = LocalContext.current
    val locationProvider = GpsMyLocationProvider(context)
    locationProvider.locationUpdateMinTime = 1000L
    val locationOverlay =
        object : MyLocationNewOverlay(GpsMyLocationProvider(context), mapView) {
            override fun onLocationChanged(location: Location?, source: IMyLocationProvider?) {
                super.onLocationChanged(location, source)
                location?.let {
                        viewModel.onUserLocationChange(GeoPoint(it.latitude, it.longitude))
                        Log.d("MapView", "onLocationChanged: ${it.latitude}, ${it.longitude}")
                }
            }
        }
    locationOverlay.enableMyLocation()
    locationOverlay.enableFollowLocation()
    mapView.overlays.add(locationOverlay)

    return locationOverlay

}

fun addTruckMarkersToMap(mapView: MapView, trucks: List<Truck>) {
    for (truck in trucks) {
        truck.latd?.let { lat ->
            truck.lgtd?.let { lng ->
                val truckGeoPoint = GeoPoint(lat, lng)
                val truckMarker = Marker(mapView).apply {
                    position = truckGeoPoint
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = truck.nameTruck
                    snippet = truck.categorie
                    subDescription = truck.adresse
                    icon = truck.categorie?.let { setMarkerColor(mapView.context, it) }
                }
                mapView.overlays.add(truckMarker)
            }
        }
    }
}

fun setMarkerColor(context: Context, categorie: String): Drawable? {
    return when (categorie) {
        "Italien/Pizza" -> getDrawable(context, R.drawable.ic_truck).apply {
            this?.setTint(setColorToTruck(context, categorie))
        }

        "Asiatique" -> getDrawable(context, R.drawable.ic_truck).apply {
            this?.setTint(setColorToTruck(context, categorie))
        }

        "Africain" -> getDrawable(context, R.drawable.ic_truck).apply {
            this?.setTint(setColorToTruck(context, categorie))
        }

        "Kebab" -> getDrawable(context, R.drawable.ic_truck).apply {
            this?.setTint(setColorToTruck(context, categorie))
        }

        "Japonais" -> getDrawable(context, R.drawable.ic_truck).apply {
            this?.setTint(setColorToTruck(context, categorie))
        }

        "Burger" -> getDrawable(context, R.drawable.ic_truck).apply {
            this?.setTint(setColorToTruck(context, categorie))
        }

        else -> getDrawable(context, R.drawable.ic_truck).apply {
            this?.setTint(setColorToTruck(context, categorie))
        }
    }
}

fun setColorToTruck(context: Context, categorie: String): Int {
    return when (categorie) {
        "Italien/Pizza" -> context.resources.getColor(R.color.pink_dark)
        "Asiatique" -> context.resources.getColor(R.color.purple_200)
        "Africain" -> context.resources.getColor(R.color.purple_500)
        "Kebab" -> context.resources.getColor(R.color.orange)
        "Japonais" -> context.resources.getColor(R.color.lite_red)
        "Burger" -> context.resources.getColor(R.color.red_dark)
        else -> context.resources.getColor(R.color.pink)
    }
}

fun centerMapOnUserLocation(
    mLocationOverlay: MyLocationNewOverlay?,
    mapView: MapView,
    onUserLocation: (GeoPoint) -> Unit
) {
    mLocationOverlay?.myLocation?.let { userLocation ->
        val geoPoint = GeoPoint(userLocation.latitude, userLocation.longitude)
        mapView.controller?.animateTo(geoPoint, 15.5, 1)

        onUserLocation(geoPoint)
    }
}

fun centerMapOnTruckLocation(
    truckId: String?,
    mapView: MapView,
    viewModel: TrucksListViewModel
) {
    if (truckId != null) {
        viewModel.getTruckById(truckId)?.let { selectedTruck ->
            mapView.controller?.animateTo(
                selectedTruck.let { GeoPoint(it.latd!!, it.lgtd!!) },
                16.0,
                1
            )
        }
    }
}
