package com.toulousehvl.myfoodtruck.ui.theme.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toulousehvl.myfoodtruck.R

@Composable
fun InputDialog(
    dialogTitle: String,
    address: String,
    nameTruck: String,
    category: String,
    onTextAddressChange: (String) -> Unit,
    onTextNameChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    show: Boolean,
    categories: List<String>,
    showError: Boolean,
    onShowErrorChange: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = { !show },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dialogTitle, textAlign = TextAlign.Center,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onTextAddressChange("")
                    onTextNameChange("")
                    onCategorySelected("")
                    onShowErrorChange(false)
                }
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        text = {
            Column {
                OutlinedTextField(
                    readOnly = true,
                    value = address,
                    onValueChange = onTextAddressChange,
                    label = { Text(text = stringResource(id = R.string.address)) },
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                )
                CustomTextField(
                    value = nameTruck,
                    onValueChange = onTextNameChange,
                    label = stringResource(id = R.string.name_max_characters, 30),
                    errorMessage = if (showError) stringResource(R.string.name_field) else null,
                    maxLength = 30,
                    singleLine = true
                )
                DropdownMenuWithFocus(
                    selectedCategory = category,
                    categories = categories,
                    onCategorySelected = onCategorySelected,
                    errorMessage = if (showError) stringResource(R.string.error_selected_category) else null
                )
            }
        }
    )
}