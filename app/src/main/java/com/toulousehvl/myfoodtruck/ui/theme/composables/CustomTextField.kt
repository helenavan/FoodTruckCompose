package com.toulousehvl.myfoodtruck.ui.theme.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.toulousehvl.myfoodtruck.R

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String? = null,
    maxLength: Int,
    singleLine: Boolean,
    maxLines: Int? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) {
                onValueChange(newValue.trimStart { it == '0' }.trimEnd { it == '0' })
            }
        },
        label = { Text(text = label) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Effacer")
                }
            }
        },
        singleLine = singleLine,
        isError = errorMessage != null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        maxLines = maxLines ?: 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.teal_700),
            unfocusedBorderColor = colorResource(id = R.color.teal_700)
        )
    )

    errorMessage?.let {
        Text(
            text = it,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )
    }
}