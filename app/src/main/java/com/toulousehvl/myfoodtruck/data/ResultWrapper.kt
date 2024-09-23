package com.toulousehvl.myfoodtruck.data

sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val exception: Exception?) : ResultWrapper<Nothing>()
    data class Loading(val isLoading: Boolean) : ResultWrapper<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            is Loading -> "Loading[isLoading=$isLoading]"
        }
    }
}