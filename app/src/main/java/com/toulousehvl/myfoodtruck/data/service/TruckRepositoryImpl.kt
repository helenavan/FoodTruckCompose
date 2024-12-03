package com.toulousehvl.myfoodtruck.data.service

import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.TruckConstants.TRUCK_COLLECTION_NAME
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TruckRepositoryImpl @Inject constructor() : TruckFirestoreRepository {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val truckCollection = firestoreInstance.collection(TRUCK_COLLECTION_NAME)

    override suspend fun getTrucksList(): ResultWrapper<List<Truck>> = withContext(ioDispatcher) {
        try {
            ResultWrapper.Success(truckCollection.get().await().toObjects(Truck::class.java))
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }

    override suspend fun addTruck(truck: Truck): ResultWrapper<Void?> = withContext(ioDispatcher) {
        try {
            val document = truckCollection.document()
            truck.documentId = document.id
            document.set(truck).await()
            ResultWrapper.Success(null)
        } catch (e: Exception) {
            ResultWrapper.Error(e)
        }
    }
}