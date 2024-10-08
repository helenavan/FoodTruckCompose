package com.toulousehvl.myfoodtruck

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _loaderUiState = MutableStateFlow<ResultWrapper<Truck>>(ResultWrapper.Loading(true))
    val loaderUiState: StateFlow<ResultWrapper<Truck>> = _loaderUiState

    private val _dataListTrucksState = MutableStateFlow<List<Truck>>(emptyList())
    val dataListTrucksState: StateFlow<List<Truck>> = _dataListTrucksState

    val selectedTruckState: MutableState<String?> = mutableStateOf(savedStateHandle["selectedTruck"])


    //fetch data from firestore
    private val truckInfo: Flow<Truck?> = flow {
        dataListTrucksState.collect { trucks ->
            val selectedTruck = trucks.find { it.documentId == selectedTruckState.value }
            emit(selectedTruck)
        }
    }


    init {
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("foodtrucks")
            .get()
            .addOnSuccessListener { result ->
                val dataList = result.documents.mapNotNull { document ->
                    document.toObject(Truck::class.java)?.copy(document.id)
                }
                _dataListTrucksState.value = dataList

                if (dataList.isNotEmpty()) {
                    _loaderUiState.value = ResultWrapper.Success(dataList.first())
                } else {
                    _loaderUiState.value = ResultWrapper.Error(Exception("No data found"))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
                _loaderUiState.value = ResultWrapper.Error(exception)
            }
    }

    fun addDataToFirestore(data: String) {
        val db = FirebaseFirestore.getInstance()

        val newData = hashMapOf(
            "fieldName" to data
        )

        db.collection("myCollection")
            .add(newData)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot added with ID: ${it.id}")
                fetchDataFromFirestore()
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
            }
    }

    fun updateSelectedTruck(truck: Truck) {
        selectedTruckState.value = truck.documentId
        savedStateHandle["selectedTruck"] = truck
        Log.d("MainViewModel", "updateSelectedTruck ===> ${selectedTruckState.value}")
    }
}