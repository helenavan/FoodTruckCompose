package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import org.osmdroid.util.GeoPoint
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

    var searchtext by mutableStateOf("")
        private set

    val searchResults: StateFlow<List<Truck>> =
        snapshotFlow { searchtext }
            .debounce(1000)
            .combine(_dataListTrucksState) { searchText, dataList ->
                when {
                    searchText.isEmpty() -> dataList
                    else -> dataList.filter {
                        (it.nameTruck?.contains(
                            searchText,
                            ignoreCase = true
                        ) == true)
                                || (it.city?.contains(searchText, ignoreCase = true) == true)
                    }
                }
            }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
            )

    private val _userTruckLocation = MutableStateFlow<GeoPoint?>(null)
    val userTruckLocation: StateFlow<GeoPoint?> =_userTruckLocation

    init {
        fetchDataFromFirestore()
    }

    fun onSearchTextChange(newText: String) {
        searchtext = newText
    }

    fun setUserLocation(newLocation: GeoPoint) {
        _userTruckLocation.value = newLocation
        Log.d("TrucksListViewModel", "=== User location: $userTruckLocation new: $newLocation")
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
        val ref = FirebaseFirestore.getInstance().collection("foodtrucks").document(id)
        ref.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                selectedTruckState.value = document.toObject(Truck::class.java)
            }
        }
        return selectedTruckState.value
    }
}