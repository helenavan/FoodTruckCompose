package com.toulousehvl.myfoodtruck

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.toulousehvl.myfoodtruck.navigation.NavigationItem
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.onPermissionDenied
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.onPermissionGranted
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.onPermissionsRevoked
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.permissionResultText
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.showPermissionResultText
import com.toulousehvl.myfoodtruck.ui.theme.composables.RequestLocationPermission
import com.toulousehvl.myfoodtruck.ui.theme.composables.ShowDialogPermission
import com.toulousehvl.myfoodtruck.ui.theme.screens.InformationScreen
import com.toulousehvl.myfoodtruck.ui.theme.screens.MapView
import com.toulousehvl.myfoodtruck.ui.theme.screens.TrucksListScreen
import com.toulousehvl.myfoodtruck.ui.theme.theme.MyFoodTruckTheme
import com.toulousehvl.myfoodtruck.ui.theme.theme.YellowBanane
import com.toulousehvl.myfoodtruck.ui.theme.theme.YellowLite
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  enableEdgeToEdge()
        setContent {

            Log.d("MainActivity", "onCreate ===> ${viewModel.selectedTruckState.value?.nameTruck}")

            val navController = rememberNavController()
            MyFoodTruckTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestLocationPermission(
                        onPermissionGranted = {
                            onPermissionGranted()
                        },
                        onPermissionDenied = {
                            onPermissionDenied()
                        },
                        onPermissionsRevoked = {
                            onPermissionsRevoked()
                        }
                    )

                    ShowDialogPermission(
                        showPermissionResultText,
                        permissionResultText
                    )
                    MainScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier, containerColor = YellowBanane) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        0.dp,
                        0.dp,
                        0.dp,
                        innerPadding.calculateBottomPadding()
                    )
                )
        ) {
            Navigations(navController, viewModel = viewModel)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Map,
        NavigationItem.ListTrucks,
        NavigationItem.Infos,
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.Map.route) }

    items.forEachIndexed { index, navigationItem ->
        if (navigationItem.route == currentRoute) {
            selectedItem = index
        }
    }

    NavigationBar(containerColor = YellowBanane) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                modifier = Modifier.background(color = YellowBanane),
                alwaysShowLabel = true,
                icon = {
                    Icon(item.icon!!, contentDescription = item.title, tint = Color.Black)
                },
                label = { Text(item.title, color = Color.Gray) },
                selected = selectedItem == index,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = YellowLite
                ),
                onClick = {
                    selectedItem = index
                    currentRoute = item.route
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            Log.d("BottomNavigationBar", "selectedItem ===> $selectedItem + currentRoute ===> $currentRoute")
        }
    }
}

@Composable
fun Navigations(navController: NavHostController, viewModel: MainViewModel) {

    Log.d("Navigations", "selectedTruckState ===> ${viewModel.selectedTruckState.value}")

    NavHost(navController, startDestination = NavigationItem.Map.route) {

        composable(
            NavigationItem.Map.route.plus("/{documentId}"),
            arguments = listOf(navArgument("documentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val truckId = backStackEntry.arguments?.getString("documentId")
            Log.d("Navigations", "documentId ===> $truckId")
            MapView(truckId = truckId, viewModel = viewModel, navController)
        }

        composable(NavigationItem.Map.route) {
            MapView("", viewModel = viewModel, navController)
        }

        composable(NavigationItem.ListTrucks.route) {
            TrucksListScreen(navController = navController)
        }

        composable(NavigationItem.Infos.route) {
            InformationScreen()
        }
    }
}

@Composable
fun CenterText(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, fontSize = 32.sp)
    }
}