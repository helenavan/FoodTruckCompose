package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
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

    private var selectedTruckState = mutableStateOf<Truck?>(null)

    init {
        fetchDataFromFirestore()
    }

    fun fetchDataFromFirestore() {
        val currentDateTime =
            ZonedDateTime.now(ZoneId.systemDefault())  // Utilise la zone horaire locale
        val twoHoursAgo = currentDateTime.minusHours(2).toInstant().toEpochMilli()

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("foodtrucks")

        docRef
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    _loaderUiState.value = ResultWrapper.Error(e)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty) {
                    val dataList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Truck::class.java)?.copy(documentId = document.id)
                    }
                    _dataListTrucksState.value =
                        dataList.filter { it.date != null && it.date!! >= twoHoursAgo }
                }
                _loaderUiState.value = ResultWrapper.Success("ok")

            }
    }

    fun getTruckById(id: String): Truck? {
        Log.d("TrucksListViewModel", "getTruckById ===> $id")
        val ref = FirebaseFirestore.getInstance().collection("foodtrucks").document(id)
        ref.get().addOnSuccessListener { document ->

            Log.d("TrucksListViewModel", "document ===> ${document}")
            if (document != null && document.exists()) {
                selectedTruckState.value = document.toObject(Truck::class.java)
            }
        }
        Log.d("TrucksListViewModel", "selectedTruckState ===> ${selectedTruckState.value}")
        return selectedTruckState.value
    }
}