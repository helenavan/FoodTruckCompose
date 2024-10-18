package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.model.Truck

@HiltViewModel
class InformationViewModel @Inject constructor() : ViewModel() {

   var _truckName by mutableStateOf("")
       private set

    fun setTruckName(name: String) {
        _truckName = name
    }

    fun addDataToFirestore(truck: Truck) {
        val db = FirebaseFirestore.getInstance()
        db.collection("foodtrucks")
            .add(truck)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added with ID: ${it.id} ===")
               // fetchDataFromFirestore()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document ===", e)
            }
    }
}