package com.toulousehvl.myfoodtruck.data.utils

import android.content.Context
import android.location.Geocoder
import org.osmdroid.util.GeoPoint
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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
        fun getCityFromGeoPoint(context: Context, geoPoint: GeoPoint): String? {
            var cityName: String? = null
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val addresses = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    cityName = address.locality
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return cityName
        }

        fun distanceFoodTruckAndUser(geoPointUser: GeoPoint, geoPointFoodTruck: GeoPoint): Double {
            val earthRadius = 6371.0
            val latUser = geoPointUser.latitude
            val lonUser = geoPointUser.longitude
            val latFoodTruck = geoPointFoodTruck.latitude
            val lonFoodTruck = geoPointFoodTruck.longitude

            val dLat = Math.toRadians(latFoodTruck - latUser)
            val dLon = Math.toRadians(lonFoodTruck - lonUser)

            val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(latUser)) * cos(
                Math.toRadians(latFoodTruck)
            ) * sin(dLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return earthRadius * c // Distance en kilomètres

        }
    }
}