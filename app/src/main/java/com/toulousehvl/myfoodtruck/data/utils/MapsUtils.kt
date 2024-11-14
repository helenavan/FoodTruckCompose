package com.toulousehvl.myfoodtruck.data.utils

import android.content.Context
import android.location.Geocoder
import org.osmdroid.util.GeoPoint
import java.util.Locale

class MapsUtils {
    companion object {
        fun getAddressFromGeoPoint(context: Context, geoPoint: GeoPoint): String? {
            val geocoder = Geocoder(context, Locale.getDefault())
            return try {
                val addresses = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    // Concaténer les différents champs pour obtenir une adresse complète
                    "${address.getAddressLine(0)}"
                } else {
                    null // Pas d'adresse trouvée
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}