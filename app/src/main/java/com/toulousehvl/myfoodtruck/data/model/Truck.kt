package com.toulousehvl.myfoodtruck.data.model

import kotlinx.serialization.Serializable


@Serializable
data class Truck(
    var documentId: String? = null,
    var nameTruck: String? = null,
    var categorie: String? = null,
    var rating:Int? = 0,
    var lgtd: Int? = 0,
    var latd: Int? = 0,
    var date:Long? = null,
    var adresse:String? = null,
    var num:String? = null,
    var street:String? = null,
    var zipCode: String? = null,
    var city:String? = null,
    var country:String? = null
)

@Serializable
sealed class CategoryTruck {
    data object Italian : CategoryTruck()
    data object Burger : CategoryTruck()
    data object Asian : CategoryTruck()
    data object Sushi : CategoryTruck()
    data object African : CategoryTruck()
    data object Kebab : CategoryTruck()

    @Serializable
    companion object {
        fun String?.toCategoryTruck(): CategoryTruck? {
            return when (this) {
                "Italien/Pizza" -> Italian
                "Burger" -> Burger
                "Thaï" -> Asian
                "Sushi" -> Sushi
                "African" -> African
                "Kebab" -> Kebab
                else -> null
            }
        }

        fun CategoryTruck.toCategoryTruckString(): String {
            return when (this) {
                is Italian -> "Italien/Pizza"
                is Burger -> "Burger"
                is Asian -> "Thaï"
                is Sushi -> "Sushi"
                is African -> "African"
                is Kebab -> "Kebab"
                else -> "Burger"
            }
        }
    }

}