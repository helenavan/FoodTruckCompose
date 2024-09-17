package com.toulousehvl.myfoodtruck

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.toulousehvl.myfoodtruck.data.UserPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

/**
 * UI state for the Home screen
 */
sealed interface FoodTruckUserUiState {
    data class Success(val userPosition: UserPosition) : FoodTruckUserUiState
    data object Error : FoodTruckUserUiState
    data object Loading : FoodTruckUserUiState
}

class MainViewModel : ViewModel() {
    //TODO
    var foodTruckUserUiState: FoodTruckUserUiState by mutableStateOf(FoodTruckUserUiState.Loading)
        private set

    var mapController: MapController? = null
        private set

    // MutableState pour suivre la position de l'utilisateur
    private val _userLocation = MutableStateFlow(GeoPoint(43.5911323, 1.4519989))
    val userLocation: StateFlow<GeoPoint> = _userLocation.asStateFlow()

    private var mLocationOverlay: MyLocationNewOverlay? = null

    var locationText by mutableStateOf("No location obtained :(")
        private set
    var permissionResultText by mutableStateOf("Permission Granted...")
        private set
    var showPermissionResultText by mutableStateOf(false)
        private set

    fun onPermissionGranted(
        context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        showPermissionResultText = true

        Log.d("permission text", "===> $permissionResultText")

//        getCurrentLocation(fusedLocationProviderClient, context, onGetCurrentLocationSuccess = {
//            locationText =
//                "Location using CURRENT-LOCATION: LATITUDE: ${it.first}, LONGITUDE: ${it.second}"
//
//        }, onGetCurrentLocationFailed = {
//            showPermissionResultText = true
//            locationText = it.localizedMessage ?: "Error Getting Current Location"
//        })
    }

    fun onPermissionDenied() {
        showPermissionResultText = true
        permissionResultText = "Permission Denied :("
    }

    fun onPermissionsRevoked() {
        showPermissionResultText = true
        permissionResultText = "Permission Revoked :("
    }

    fun createMap(context: Context, map: MapView) {
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.overlays.add(CopyrightOverlay(context))

        mapController = MapController(map).apply {
            mapController?.animateTo(userLocation.value, 16.0, 2)
        }

        val locationProvider = GpsMyLocationProvider(context)

        // Initialisation de l'overlay de localisation
        mLocationOverlay = MyLocationNewOverlay(locationProvider, map).apply {
            enableMyLocation()
            enableFollowLocation()
            startLocationUpdates()
        }
        mapController?.animateTo(userLocation.value, 16.0, 2)
        map.overlays.add(mLocationOverlay)
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            while (isActive) {
                handleNewLocation()
                delay(10000L)
            }
        }
    }

    private fun handleNewLocation() {
        val lastKnownLocation = mLocationOverlay?.myLocationProvider?.lastKnownLocation
        lastKnownLocation?.let {
            _userLocation.value = GeoPoint(it.latitude, it.longitude)
        }
        //get Geoposition of user
        mapController?.animateTo(userLocation.value, 16.5, 5)
        Log.d("lastKnow", "===> ${_userLocation.value}")
    }

}