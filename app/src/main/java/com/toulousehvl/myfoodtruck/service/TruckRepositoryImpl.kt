package com.toulousehvl.myfoodtruck.service

import com.toulousehvl.myfoodtruck.data.Truck

class TruckRepositoryImpl : TruckRepository {

    private val _trucks = arrayListOf<Truck>()

    override fun findTruck(lat: Double, lon: Double): Truck? {
      //  TODO("Not yet implemented")
        return _trucks.firstOrNull { it.latd == lat }
    }
}