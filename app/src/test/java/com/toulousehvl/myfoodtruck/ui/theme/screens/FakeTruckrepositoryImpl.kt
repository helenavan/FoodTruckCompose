package com.toulousehvl.myfoodtruck.ui.theme.screens

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck
import com.toulousehvl.myfoodtruck.data.service.TruckFirestoreRepository

//TODO ===
open class FakeTruckRepositoryImpl : TruckFirestoreRepository {
    private val truckList = mutableListOf<Truck>()

    override suspend fun getTrucksList(): Task<MutableList<Truck>> {
        return Tasks.forResult(truckList)
    }

    override suspend fun addTruck(truck: Truck): Task<Void> {
        truckList.add(truck)
        return Tasks.forResult(null)
    }
}