package com.toulousehvl.myfoodtruck

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.toulousehvl.myfoodtruck.data.UserPosition
import org.osmdroid.util.GeoPoint

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
    var locationFoodTrucks by mutableStateOf(GeoPoint(48.8583, 2.2944))
        private set //private set est utilisé pour rendre une variable publique en lecture et privée en écriture

}