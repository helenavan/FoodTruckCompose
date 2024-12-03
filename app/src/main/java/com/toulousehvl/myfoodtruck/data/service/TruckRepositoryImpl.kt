package com.toulousehvl.myfoodtruck.data.service

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.TruckConstants.TRUCK_COLLECTION_NAME
import com.toulousehvl.myfoodtruck.data.model.Truck
import javax.inject.Inject

class TruckRepositoryImpl @Inject constructor() : TruckFirestoreRepository {

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val truckCollection = firestoreInstance.collection(TRUCK_COLLECTION_NAME)

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

    override fun addTruck(truck: Truck): Task<Void> {
        val document = truckCollection.document()
        truck.documentId = document.id
        return document.set(truck)
    }
}