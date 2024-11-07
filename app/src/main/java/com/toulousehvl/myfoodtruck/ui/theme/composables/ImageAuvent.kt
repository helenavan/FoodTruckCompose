package com.toulousehvl.myfoodtruck.ui.theme.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.toulousehvl.myfoodtruck.R

@Composable
fun TopImage() {
    Image(painter = painterResource(id = R.drawable.auvent), contentDescription = "",modifier = Modifier
        .fillMaxWidth().padding(bottom = 10.dp))
}