package com.toulousehvl.myfoodtruck.ui.theme.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@HiltViewModel
class InformationViewModel @Inject constructor() : ViewModel() {

   var _truckName by mutableStateOf("")
       private set

    fun setTruckName(name: String) {
        _truckName = name
    }
}