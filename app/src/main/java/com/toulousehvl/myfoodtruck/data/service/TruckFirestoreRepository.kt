package com.toulousehvl.myfoodtruck.data.service

import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck

//TODO when backend will be ready
interface TruckFirestoreRepository {

   suspend fun getTrucksList(): ResultWrapper<List<Truck>>

   suspend fun addTruck(truck: Truck): ResultWrapper<Void?>
}