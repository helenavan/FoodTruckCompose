package com.toulousehvl.myfoodtruck

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.Truck
import com.toulousehvl.myfoodtruck.data.UserPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface FoodTruckUserUiState {
    data class Success(val userPosition: UserPosition) : FoodTruckUserUiState
    data object Error : FoodTruckUserUiState
    data object Loading : FoodTruckUserUiState
}

class MainViewModel : ViewModel() {
    //TODO
    var foodTruckUserUiState: FoodTruckUserUiState by mutableStateOf(FoodTruckUserUiState.Loading)
        private set

    // MutableState pour suivre la position de l'utilisateur
    private val _dataState = MutableStateFlow<List<Truck>>(emptyList())
    val dataState: StateFlow<List<Truck>> = _dataState

    init {
        fetchDataFromFirestore()
    }

    // Fonction pour récupérer des données Firestore
    fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("foodtrucks")
            .get()
            .addOnSuccessListener { result ->
                val dataList = result.documents.mapNotNull { document ->
                    document.toObject(Truck::class.java)?.copy(document.id)
                }
                _dataState.value = dataList

                Log.d("Firestore", "Data fetched successfully ===> $dataList")
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }

    // Fonction pour ajouter des données dans Firestore
    fun addDataToFirestore(data: String) {
        val db = FirebaseFirestore.getInstance()

        val newData = hashMapOf(
            "fieldName" to data
        )

        db.collection("myCollection")
            .add(newData)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added with ID: ${it.id}")
                fetchDataFromFirestore() // Optionnel, mettre à jour les données
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }
}