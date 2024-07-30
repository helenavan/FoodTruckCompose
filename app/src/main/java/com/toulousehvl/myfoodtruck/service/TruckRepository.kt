package com.toulousehvl.myfoodtruck.service

import com.toulousehvl.myfoodtruck.data.Truck

interface TruckRepository {
    fun findTruck(lat: Double, lon: Double): Truck?
}