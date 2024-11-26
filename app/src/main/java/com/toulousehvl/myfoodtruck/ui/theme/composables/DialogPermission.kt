package com.toulousehvl.myfoodtruck.ui.theme.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.toulousehvl.myfoodtruck.R

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
            text = stringResource(R.string.location_permission),
            textAlign = TextAlign.Center
        )

        // Display permission result
        if (showPermissionResultText) {
            Text(text = permissionResultText, textAlign = TextAlign.Center)
        }
    }
}