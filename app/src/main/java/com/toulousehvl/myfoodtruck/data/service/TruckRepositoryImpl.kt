package com.toulousehvl.myfoodtruck.data.service

import com.toulousehvl.myfoodtruck.data.model.Truck

class TruckRepositoryImpl : TruckRepository {

    private val _trucks = arrayListOf<Truck>()

    override fun findTruck(lat: Double, lon: Double): Truck? {
      //  TODO("Not yet implemented")
        return _trucks.firstOrNull { it.latd == lat }
    }
}