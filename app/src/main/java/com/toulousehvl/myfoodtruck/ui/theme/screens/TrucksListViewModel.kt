package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.TruckConstants.TRUCK_COLLECTION_NAME
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck
import com.toulousehvl.myfoodtruck.data.service.TruckRepositoryImpl
import com.toulousehvl.myfoodtruck.data.utils.MapsUtils.Companion.filterFoodTrucks
import com.toulousehvl.myfoodtruck.data.utils.MapsUtils.Companion.getLatLngFromAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class TrucksListViewModel @Inject constructor(private val truckRepositoryImpl: TruckRepositoryImpl) :
    ViewModel() {

    private val _loaderUiState =
        MutableStateFlow<ResultWrapper<Truck>>(ResultWrapper.Loading(false))
    val loaderUiState: StateFlow<ResultWrapper<Truck>> = _loaderUiState

    private val _dataListTrucksState = MutableStateFlow<List<Truck>>(emptyList())
    val dataListTrucksState: StateFlow<List<Truck>> = _dataListTrucksState

    private var selectedTruckState = mutableStateOf<Truck?>(null)

    var selectedCategory by mutableStateOf("")
        private set

    var foodTruckName by mutableStateOf("")
        private set

    var foodTruckAddress by mutableStateOf("")
        private set

    var showError by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var userLocation by mutableStateOf<GeoPoint?>(null)
        private set

    fun onCategorySelected(newCategory: String) {
        selectedCategory = newCategory
    }

    fun onFoodTruckAddressChange(newAddress: String) {
        foodTruckAddress = newAddress
    }

    fun onFoodTruckNameChange(newName: String) {
        foodTruckName = newName
    }

    fun onShowError(newError: Boolean) {
        showError = newError
    }

    fun onUserLocationChange(newLocation: GeoPoint?) {
            userLocation = newLocation
            _dataListTrucksState.value = 5.0.filterFoodTrucks(
                _dataListTrucksState.value, userLocation
            )
    }

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
                        ) == true) || (it.city?.contains(searchText, ignoreCase = true) == true)
                    }
                }
            }
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
            )

    init {
        fetchDataFromFirestore()
    }

    fun onSearchTextChange(newText: String) {
        searchtext = newText
    }

    fun fetchDataFromFirestore() {
        viewModelScope.launch {
            _loaderUiState.value = ResultWrapper.Loading(true)
            when (val result = truckRepositoryImpl.getTrucksList()) {
                is ResultWrapper.Success -> {
                    _dataListTrucksState.value = 5.0.filterFoodTrucks(
                        result.data, userLocation
                    )
                    _loaderUiState.value = ResultWrapper.Success(Truck())
                }

                is ResultWrapper.Error -> {
                    _loaderUiState.value = ResultWrapper.Error(result.exception)
                }

                is ResultWrapper.Loading -> {
                    _loaderUiState.value = ResultWrapper.Loading(true)
                }
            }
        }
    }

    fun getTruckById(id: String): Truck? {
        val ref = FirebaseFirestore.getInstance().collection(TRUCK_COLLECTION_NAME).document(id)
        ref.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                selectedTruckState.value = document.toObject(Truck::class.java)
            }
        }
        return selectedTruckState.value
    }

    fun addFoodTruckToFirestore(context: Context) {

        if (foodTruckName.isEmpty() || foodTruckName.isEmpty() || selectedCategory.isEmpty()) {
            showError = true
            return
        }

        showError = false

        viewModelScope.launch {
           val result = getLatLngFromAddress(context, foodTruckAddress)
            result?.let {
                addDataToFirestore(
                    Truck(
                        nameTruck = foodTruckName,
                        categorie = selectedCategory,
                        latd = it.latitude,
                        lgtd = it.longitude,
                        date = System.currentTimeMillis(),
                        street = it.thoroughfare,
                        zipCode = it.postalCode,
                        city = it.locality,
                        country = it.countryName,
                        adresse = foodTruckAddress,
                        num = it.subThoroughfare
                    )
                )
            }
        }
    }

    private suspend fun addDataToFirestore(truck: Truck) {
        isLoading = true
        when (val result = truckRepositoryImpl.addTruck(truck)) {
            is ResultWrapper.Success -> {
                fetchDataFromFirestore()
                foodTruckName = ""
                foodTruckAddress = ""
                selectedCategory = ""
                isLoading = false

                fetchDataFromFirestore()
            }

            is ResultWrapper.Error -> {
                Log.e("TrucksListViewModel", "Error adding document")
            }

            is ResultWrapper.Loading -> {
                isLoading = true
            }
        }
        isLoading = false
    }
}