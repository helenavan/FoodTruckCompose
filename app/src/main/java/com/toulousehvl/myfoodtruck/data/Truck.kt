package com.toulousehvl.myfoodtruck.data

data class Truck(
    var documentId: String? = null,
    var nameTruck:String? = null,
    var categorie:String? = null,
    var rating:Double? = 0.0,
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
