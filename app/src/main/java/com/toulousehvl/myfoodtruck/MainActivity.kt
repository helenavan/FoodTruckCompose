package com.toulousehvl.myfoodtruck

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.Configuration
import com.toulousehvl.myfoodtruck.navigation.NavigationItem
import com.toulousehvl.myfoodtruck.screens.HomeScreen
import com.toulousehvl.myfoodtruck.screens.InformationScreen
import com.toulousehvl.myfoodtruck.screens.TrucksListScreen
import com.toulousehvl.myfoodtruck.ui.theme.MyFoodTruckTheme
import com.toulousehvl.myfoodtruck.ui.theme.YellowBanane
import com.toulousehvl.myfoodtruck.ui.theme.YellowLite

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyFoodTruckTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier, containerColor = YellowBanane) {
                BottomNavigationBar(navController = navController)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = YellowBanane
            ) {
                Icon(Icons.Filled.Add, "Add", tint = Color.Black)
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
            Navigations(navController = navController)
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.History,
        NavigationItem.Profile,
    )
    var selectedItem by remember { mutableStateOf(0) }
    var currentRoute by remember { mutableStateOf(NavigationItem.Home.route) }

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
        }
    }
}

@Composable
fun Navigations(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen(LocalContext.current)
        }
        composable(NavigationItem.History.route) {
            TrucksListScreen()
        }
        composable(NavigationItem.Profile.route) {
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

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES,
    name = "GreetingPreviewDark"
)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyFoodTruckTheme {
       // MainScreen(navController = rememberNavController())
    }
}