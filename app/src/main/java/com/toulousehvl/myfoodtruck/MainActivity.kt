package com.toulousehvl.myfoodtruck

import android.os.Bundle
import android.view.Window
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.toulousehvl.myfoodtruck.navigation.BottomNavigationBar
import com.toulousehvl.myfoodtruck.navigation.TrucksNavGraph
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.onPermissionDenied
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.onPermissionGranted
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.onPermissionsRevoked
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.permissionResultText
import com.toulousehvl.myfoodtruck.ui.theme.composables.PermissionResultTex.showPermissionResultText
import com.toulousehvl.myfoodtruck.ui.theme.composables.RequestLocationPermission
import com.toulousehvl.myfoodtruck.ui.theme.composables.ShowDialogPermission
import com.toulousehvl.myfoodtruck.ui.theme.theme.MyFoodTruckTheme
import com.toulousehvl.myfoodtruck.ui.theme.theme.YellowBanane
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetStatusBarColor(this)
            val navController = rememberNavController()
            MyFoodTruckTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
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
                    MainScreen(navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White
            ),
                title = {
                    DisplayLogo()
                }
            )
        },
        bottomBar = {
            BottomAppBar(modifier = Modifier, containerColor = YellowBanane) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Column {
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
                TrucksNavGraph(
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun BannerConnexionError(mainViewModel: MainViewModel = hiltViewModel()) {
    val connectionState by mainViewModel.isInternetAvailable.collectAsStateWithLifecycle()

    if (!connectionState) {
        Row {
            Text(
                text = "Pas de connexion internet",
                modifier = Modifier
                    .padding(1.dp)
                    .fillMaxWidth()
                    .background(Color.Red),
                color = Color.White, textAlign = TextAlign.Center
            )
        }
    }
}

fun ComponentActivity.setStatusBarColor(
    color: Color,
    darkIcons: Boolean = false
) {
    // Get the window from the current activity
    val window: Window = this.window

    window.statusBarColor = color.toArgb()

    // Check if we're on Android 11 (API 30) or later to set the status bar icon color
    val controller = window.insetsController
    if (controller != null) {
        controller.setSystemBarsAppearance(
            if (darkIcons) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }
}

@Composable
fun SetStatusBarColor(activity: ComponentActivity) {
    activity.setStatusBarColor(colorResource(R.color.teal_700), darkIcons = false)
}

@Composable
fun DisplayLogo() {
    Column {
        BannerConnexionError()
        Spacer(modifier = Modifier.height(6.dp))
        Image(
            modifier = Modifier
                .padding(4.dp, 2.dp, 2.dp, 4.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.logo_fd_logo),
            contentDescription = "frites",
            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(colorResource(id = R.color.teal_700))
        )
    }
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MyFoodTruckTheme {

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
            title = {
                DisplayLogo()
            },
            modifier = Modifier.shadow(
                elevation = 2.dp
            )
        )
    }
}

@Preview
@Composable
fun DisplayLogoPreview() {
    DisplayLogo()
}
