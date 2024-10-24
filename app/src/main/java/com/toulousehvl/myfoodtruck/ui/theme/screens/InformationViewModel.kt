package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.content.Context
import android.location.Address
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.model.Truck
import dagger.hilt.android.lifecycle.HiltViewModel
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

    var result by mutableStateOf<Address?>(null)
        private set

    var showError by mutableStateOf(false)
        private set

    fun onTruckNameChange(newName: String) {
        truckName = newName
    }

    fun onTruckAddressChange(newAddress: String) {
        truckAddress = newAddress
    }

    fun onCategorySelected(newCategory: String) {
        selectedCategory = newCategory
    }

    fun addFoodTruckToFirestore(context: Context) {

        if (truckName.isEmpty() || truckAddress.isEmpty() || selectedCategory.isEmpty()) {
            showError = true
            return
        }

        showError = false

        viewModelScope.launch {
            result = getLatLngFromAddress(context, truckAddress)
            result?.let {
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
            }
        }
    }

    private fun addDataToFirestore(truck: Truck) {
        isLoading = true
        val db = FirebaseFirestore.getInstance()
        db.collection("foodtrucks")
            .add(truck)
            .addOnSuccessListener {
                //TODO update the list of foodtrucks ?
                // fetchDataFromFirestore()
                truckName = ""
                truckAddress = ""
                selectedCategory = ""
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error adding document", e)
                isLoading = false
            }
    }
}