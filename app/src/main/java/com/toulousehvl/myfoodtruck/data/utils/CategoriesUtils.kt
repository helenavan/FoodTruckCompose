package com.toulousehvl.myfoodtruck.data.utils

import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.data.model.CategoryTruck
import com.toulousehvl.myfoodtruck.data.model.CategoryTruck.Companion.toCategoryTruckString

class CategoriesUtils {

    companion object {
        fun setTruckCategory(category: String): Int {
            return when (category) {
                CategoryTruck.Italian.toCategoryTruckString() -> R.drawable.ic_pizza
                CategoryTruck.Burger.toCategoryTruckString() -> R.drawable.ic_burger
                CategoryTruck.Asian.toCategoryTruckString() -> R.drawable.ic_thai
                CategoryTruck.Japanese.toCategoryTruckString() -> R.drawable.ic_sushi
                CategoryTruck.African.toCategoryTruckString() -> R.drawable.ic_africain
                CategoryTruck.Kebab.toCategoryTruckString() -> R.drawable.ic_kebab
                else -> R.drawable.ic_vegetarien
            }
        }
    }

}