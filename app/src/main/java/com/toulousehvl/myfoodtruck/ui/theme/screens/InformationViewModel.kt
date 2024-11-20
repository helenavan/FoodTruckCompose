package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.model.Truck
import com.toulousehvl.myfoodtruck.data.utils.MapsUtils.Companion.getLatLngFromAddress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor() : ViewModel() {

    var truckName by mutableStateOf("")
        private set

    var truckAddress by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var showError by mutableStateOf(false)
        private set

    private val _showErrorMessage = MutableSharedFlow<Boolean>()
    val showErrorMessage = _showErrorMessage.asSharedFlow()

    fun onTruckNameChange(newName: String) {
        truckName = newName
    }

    fun onTruckAddressChange(newAddress: String) {
        truckAddress = newAddress
    }

    fun onCategorySelected(newCategory: String) {
        selectedCategory = newCategory
    }

    fun clearFields() {
        truckName = ""
        truckAddress = ""
        selectedCategory = ""
        showError = false
    }

    fun addFoodTruckToFirestore(context: Context) {

        if (truckName.isEmpty() || truckAddress.isEmpty() || selectedCategory.isEmpty()) {
            showError = true
            return
        }

        showError = false

        viewModelScope.launch {
           val result = getLatLngFromAddress(context, truckAddress)
            result?.let {
                _showErrorMessage.emit(false)
                addDataToFirestore(
                    Truck(
                        nameTruck = truckName,
                        categorie = selectedCategory,
                        latd = it.latitude,
                        lgtd = it.longitude,
                        date = System.currentTimeMillis(),
                        street = it.thoroughfare,
                        zipCode = it.postalCode,
                        city = it.locality,
                        country = it.countryName,
                        adresse = truckAddress,
                        num = it.subThoroughfare
                    )
                )
            } ?: run {
                _showErrorMessage.emit(true)
            }
        }
    }

    private fun addDataToFirestore(truck: Truck) {
        isLoading = true
        val db = FirebaseFirestore.getInstance()
        db.collection("foodtrucks")
            .add(truck)
            .addOnSuccessListener {
                clearFields()
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
                isLoading = false
            }
    }
}