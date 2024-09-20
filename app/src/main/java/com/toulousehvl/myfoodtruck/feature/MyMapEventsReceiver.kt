package com.toulousehvl.myfoodtruck.feature

import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint

class MyMapEventsReceiver : MapEventsReceiver {
    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
        return false
    }

    override fun longPressHelper(p: GeoPoint?): Boolean {
        return true
    }
}