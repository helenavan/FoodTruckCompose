package com.toulousehvl.myfoodtruck.ui.theme.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavController
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.navigation.NavigationItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SplashScreen(navController: NavController) {
    val offsetX =
        remember { androidx.compose.animation.core.Animatable(-500f) } //Commence à gauche de l'écran
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = true) {
        scope.launch {
            offsetX.animateTo(
                targetValue = 800f, // destination à droite de l'écran
                animationSpec = tween(
                    durationMillis = 2000,
                )
            )
            delay(1000L)
            navController.navigate(NavigationItem.MapTruck.route)
        }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_fd_flat),
            contentDescription = "Logo",
            // modifier = Modifier.scale(scale.value)
            modifier = Modifier.offset {
                IntOffset(offsetX.value.roundToInt(), 0)
            },
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}
