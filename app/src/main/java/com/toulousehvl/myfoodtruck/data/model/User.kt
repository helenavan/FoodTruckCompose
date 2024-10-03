package com.toulousehvl.myfoodtruck.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userName: String? = null,
    val userPosition: UserPosition
)

@Serializable
data class UserPosition(
    val userLat: Double? = null,
    val userLong: Double? = null
)