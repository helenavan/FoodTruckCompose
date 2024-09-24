package com.toulousehvl.myfoodtruck.data.service

import android.util.Log
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint

class MyMapEventsReceiver(private val onGeoPointSelected: (GeoPoint) -> Unit) : MapEventsReceiver {

    override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
       return false
    }

    override fun longPressHelper(geoPoint: GeoPoint?): Boolean {
        geoPoint?.let {
            onGeoPointSelected(it)
            Log.d("MyMapEventsReceiver", "Tap at ===> $geoPoint")
        }
        return true
    }
}