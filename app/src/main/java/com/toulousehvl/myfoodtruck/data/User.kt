package com.toulousehvl.myfoodtruck.data

data class User(
    val userName: String? = null,
    val userPosition: UserPosition
)

data class UserPosition(
    val userLat: Double? = null,
    val userLong: Double? = null
)