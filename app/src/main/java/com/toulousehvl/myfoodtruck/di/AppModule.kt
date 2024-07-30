package com.toulousehvl.myfoodtruck.di

import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.service.TruckRepository
import com.toulousehvl.myfoodtruck.service.TruckRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule {
    val appModule = module {
        single<TruckRepository> { TruckRepositoryImpl() }
        viewModel { MainViewModel() }
    }
}