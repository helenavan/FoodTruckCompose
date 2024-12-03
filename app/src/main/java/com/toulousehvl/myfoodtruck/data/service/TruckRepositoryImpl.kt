package com.toulousehvl.myfoodtruck.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.model.Truck
import javax.inject.Inject

class TruckRepositoryImpl @Inject constructor() : TruckRepository {

    private val _trucks = arrayListOf<Truck>()

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val truckCollection = firestoreInstance.collection("foodtrucks")

    override fun findTruck(lat: Double, lon: Double): Truck? {
      //  TODO("Not yet implemented")
        return _trucks.firstOrNull { it.latd?.toDouble() == lat && it.lgtd?.toDouble() == lat }
    }

    override fun getTrucksList(): Task<List<Truck>> {
        val documentSnapshot = truckCollection.get()
        return documentSnapshot.continueWith {
            if (documentSnapshot.isSuccessful) {
                return@continueWith documentSnapshot.result?.toObjects(Truck::class.java)
            } else {
                return@continueWith emptyList<Truck>()
            }
        }
    }
}