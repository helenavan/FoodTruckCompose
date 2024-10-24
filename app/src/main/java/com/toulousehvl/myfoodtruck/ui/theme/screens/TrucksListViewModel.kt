package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class TrucksListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _loaderUiState = MutableStateFlow<ResultWrapper<Truck>>(ResultWrapper.Loading(true))
    val loaderUiState: StateFlow<ResultWrapper<Truck>> = _loaderUiState

    private val _dataListTrucksState = MutableStateFlow<List<Truck>>(emptyList())
    val dataListTrucksState: StateFlow<List<Truck>> = _dataListTrucksState

    val selectedTruckState: MutableState<String?> =
        mutableStateOf(savedStateHandle["selectedTruck"])

    init {
        fetchDataFromFirestore()
    }

    fun fetchDataFromFirestore() {
        val currentDateTime =
            ZonedDateTime.now(ZoneId.systemDefault())  // Utilise la zone horaire locale
        val twoHoursAgo = currentDateTime.minusHours(2).toInstant().toEpochMilli()

        val db = FirebaseFirestore.getInstance()

        db.collection("foodtrucks")
            .get()
            .addOnSuccessListener { result ->
                val dataList = result.documents.mapNotNull { document ->
                    document.toObject(Truck::class.java)?.copy(documentId = document.id)
                }

                //filter by date
                _dataListTrucksState.value =
                    dataList.filter { it.date != null && it.date!! >= twoHoursAgo }
                //TODO filter by location
//                _dataListTrucksState.value =
//                    dataList.filter { foodtruck ->
//                        val isRecent = foodtruck.date?.let { it >= twoHoursAgo } ?: false
//                        val isNear = userLocationState?.let {
//                            val foodTruckLocation = GeoPoint(foodtruck.latd!!, foodtruck.lgtd!!)
//                            it.distanceToAsDouble(foodTruckLocation) < 10000.0
//                        } ?: false
//                        isRecent && isNear
//                    }

                _loaderUiState.value = ResultWrapper.Success("ok")

            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
                _loaderUiState.value = ResultWrapper.Error(exception)
            }
    }

    //TODO
    fun updateSelectedTruck(truck: Truck) {
        selectedTruckState.value = truck.documentId
        //  savedStateHandle["selectedTruck"] = truck
        Log.d("MainViewModel", "updateSelectedTruck ===> ${selectedTruckState.value}")
    }
}