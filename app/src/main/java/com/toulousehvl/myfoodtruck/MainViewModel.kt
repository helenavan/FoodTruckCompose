package com.toulousehvl.myfoodtruck

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toulousehvl.myfoodtruck.data.Truck
import com.toulousehvl.myfoodtruck.data.UserPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface FoodTruckUserUiState {
    data class Success(val userPosition: UserPosition) : FoodTruckUserUiState
    data object Error : FoodTruckUserUiState
    data object Loading : FoodTruckUserUiState
}

class MainViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var foodTruckUserUiState: FoodTruckUserUiState by mutableStateOf(FoodTruckUserUiState.Loading)
        private set

    private val _userLocation = MutableStateFlow(UserPosition())
    val userLocation: StateFlow<UserPosition> = _userLocation.asStateFlow()

    fun getUserGeoPoint(userPosition: UserPosition) {
       viewModelScope.launch {
           _userLocation.value = userPosition
       /**   foodTruckUserUiState = FoodTruckUserUiState.Loading
            foodTruckUserUiState = try {
                FoodTruckUserUiState.Success(userPosition)

            } catch (e: IOException) {
                FoodTruckUserUiState.Error
            }**/
        }
    }

}