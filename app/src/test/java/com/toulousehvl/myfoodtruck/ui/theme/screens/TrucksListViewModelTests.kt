package com.toulousehvl.myfoodtruck.ui.theme.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.toulousehvl.myfoodtruck.data.model.Truck
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.osmdroid.util.GeoPoint


@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
class TrucksListViewModelTests {
    private lateinit var viewModel: TrucksListViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    val mockFirestore = mockk<FirebaseFirestore>(relaxed = true)
    val mockCollection = mockk<CollectionReference>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = spyk(TrucksListViewModel())
        every { mockFirestore.collection("foodtrucks") } returns mockCollection
        every { FirebaseFirestore.getInstance() } returns mockFirestore
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onCategorySelected_should_update_selectedCategory() {
        viewModel.onCategorySelected("Burger")
        assertEquals("Burger", viewModel.selectedCategory)
    }

    @Test
    fun onFoodTruckAddressChange_should_update_foodTruckAddress() {
        viewModel.onFoodTruckAddressChange("123 Main St")
        assertEquals("123 Main St", viewModel.foodTruckAddress)
    }

    @Test
    fun onFoodTruckNameChange_should_update_foodTruckName() {
        viewModel.onFoodTruckNameChange("Test Name Truck")
        assertEquals("Test Name Truck", viewModel.foodTruckName)
    }

    @Test
    fun onUserLocationChange_should_update_userLocation_and_filter_trucks() {
        val mockTrucks = listOf(
            Truck(nameTruck = "Truck 1", categorie = "Burger", latd = 1.0, lgtd = 2.0),
            Truck(nameTruck = "Truck 2", categorie = "Pizza", latd = 3.0, lgtd = 4.0)
        )

        val newLocation = GeoPoint(43.6043, 1.4437)

        viewModel.onUserLocationChange(newLocation)
        assertEquals(newLocation, viewModel.userLocation)
        assertEquals(1, viewModel.dataListTrucksState.value)
    }

    @Test
    fun onSearchTextChange_should_update_searchText() {
        viewModel.onSearchTextChange("New Search")
        assertEquals("New Search", viewModel.searchtext)
    }

}