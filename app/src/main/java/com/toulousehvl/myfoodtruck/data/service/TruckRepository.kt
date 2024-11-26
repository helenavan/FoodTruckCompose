package com.toulousehvl.myfoodtruck.data.service

import com.toulousehvl.myfoodtruck.data.model.Truck

//TODO when backend will be ready
interface TruckRepository {
    fun findTruck(lat: Double, lon: Double): Truck?
}