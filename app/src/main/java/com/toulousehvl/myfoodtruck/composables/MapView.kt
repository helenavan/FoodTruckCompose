package com.toulousehvl.myfoodtruck.composables

import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.toulousehvl.myfoodtruck.MainViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapView(
    modifier: Modifier = Modifier,
    onLoad: ((map: MapView) -> Unit)? = null,
    viewModel: MainViewModel
) {
    val mapView = rememberMapViewWithLifecycle()
    val uiState by viewModel.userLocation.collectAsState()

    // Utilisation d'AndroidView pour intégrer le MapView dans la composition
    AndroidView(
        factory = { context ->
            // Configuration de la carte (appelé une seule fois au moment de la création)
            Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        onLoad?.invoke(mapView) // Callback une fois que la carte est prête

        // Vérification des coordonnées utilisateur pour éviter des centres invalides
        uiState.userLat?.let { lat ->
            uiState.userLong?.let { long ->
                val startPoint = GeoPoint(lat, long)
                Log.d("HomeScreen", "startPoint ===> $startPoint")
                val userMarker = Marker(mapView)
                userMarker.position = startPoint
                userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(userMarker)
                mapView.controller.setCenter(startPoint)
                mapView.controller.zoomTo(17.1)
            }
        }
    }
}