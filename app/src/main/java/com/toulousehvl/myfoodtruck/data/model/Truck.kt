package com.toulousehvl.myfoodtruck.data.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable


@Serializable
data class Truck(
    var documentId: String? = null,
    var nameTruck: String? = null,
    var categorie: String? = null,
    var rating:Int? = 0,
    var lgtd: Double? = 0.0,
    var latd: Double? = 0.0,
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
    data object Japanese : CategoryTruck()
    data object African : CategoryTruck()
    data object Kebab : CategoryTruck()
    data object Other : CategoryTruck()

    @Serializable
    companion object {
        fun String?.toCategoryTruck(): CategoryTruck? {
            return when (this) {
                "Italien/Pizza" -> Italian
                "Burger" -> Burger
                "Asiatique" -> Asian
                "Japonais" -> Japanese
                "Africain" -> African
                "Kebab" -> Kebab
                else -> Other
            }
        }

        fun CategoryTruck.toCategoryTruckString(): String {
            return when (this) {
                is Italian -> "Italien/Pizza"
                is Burger -> "Burger"
                is Asian -> "Asiatique"
                is Japanese -> "Japonais"
                is African -> "Africain"
                is Kebab -> "Kebab"
                else -> "Burger"
            }
        }
    }

}