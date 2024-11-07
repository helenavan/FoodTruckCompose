package com.toulousehvl.myfoodtruck

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
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
            SetStatusBarColorExample(this)
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

@ExperimentalMaterialApi
@Composable
fun MainScreen(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = {
            BottomAppBar(modifier = Modifier, containerColor = YellowBanane) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        Column {
            BannerConnexionError()
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
        ) {
            Text(
                text = "Pas de connexion internet",
                modifier = Modifier.padding(1.dp),
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
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val controller = window.insetsController
        if (controller != null) {
            controller.setSystemBarsAppearance(
                if (darkIcons) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    } else {
        // For Android 10 and below, set decor view system UI visibility flags
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = if (darkIcons) {
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

@Composable
fun SetStatusBarColorExample(activity: ComponentActivity) {
    activity.setStatusBarColor(colorResource(R.color.teal_700), darkIcons = false)
}
