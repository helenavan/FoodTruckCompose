package com.toulousehvl.myfoodtruck.data.service

import com.google.android.gms.tasks.Task
import com.toulousehvl.myfoodtruck.data.model.Truck

//TODO when backend will be ready
interface TruckFirestoreRepository {

    fun getTrucksList(): Task<List<Truck>>

    fun addTruck(truck: Truck): Task<Void>
}