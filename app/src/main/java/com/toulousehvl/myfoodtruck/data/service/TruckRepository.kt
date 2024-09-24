package com.toulousehvl.myfoodtruck.data.service

import com.toulousehvl.myfoodtruck.data.model.Truck

interface TruckRepository {
    fun findTruck(lat: Double, lon: Double): Truck?
}