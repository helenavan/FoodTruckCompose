package com.toulousehvl.myfoodtruck.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ShowDialogPermission(
    showPermissionResultText: Boolean,
    permissionResultText: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display a message indicating the permission request process
        Text(
            text = "Requesting location permission...",
            textAlign = TextAlign.Center
        )

        // Display permission result
        if (showPermissionResultText) {
            Text(text = permissionResultText, textAlign = TextAlign.Center)
        }
    }
}