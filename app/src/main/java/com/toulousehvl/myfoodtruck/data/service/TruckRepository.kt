package com.toulousehvl.myfoodtruck.data.service

import com.google.android.gms.tasks.Task
import com.toulousehvl.myfoodtruck.data.model.Truck

//TODO when backend will be ready
interface TruckRepository {
    fun findTruck(lat: Double, lon: Double): Truck?

    fun getTrucksList(): Task<List<Truck>>
}