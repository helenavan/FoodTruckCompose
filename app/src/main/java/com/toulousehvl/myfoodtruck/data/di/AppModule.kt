package com.toulousehvl.myfoodtruck.data.di

import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.data.service.TruckRepository
import com.toulousehvl.myfoodtruck.data.service.TruckRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single<TruckRepository> { TruckRepositoryImpl() }
        viewModel { MainViewModel() }
    }
}