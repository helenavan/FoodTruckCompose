package com.toulousehvl.myfoodtruck.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.lifecycle.ViewModel
import com.toulousehvl.myfoodtruck.MainApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _isInternetAvailable = MutableStateFlow(false)
    val isInternetAvailable: StateFlow<Boolean> = _isInternetAvailable

    fun setInternetAvailability(isAvailable: Boolean) {
        _isInternetAvailable.value = isAvailable
    }

    private fun monitorInternetConnection() {
        val connectivityManager = MainApplication.instance
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isInternetAvailable.value = true
            }

            override fun onLost(network: Network) {
                _isInternetAvailable.value = false

            }
        })
    }
}