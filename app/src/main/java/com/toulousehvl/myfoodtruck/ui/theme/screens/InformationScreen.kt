package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.ui.theme.composables.CustomTextField
import com.toulousehvl.myfoodtruck.ui.theme.composables.DropdownMenuWithFocus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(viewModel: InformationViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val truckName = viewModel.truckName
        val truckAddress = viewModel.truckAddress
        val categories =
            context.resources.getStringArray(R.array.food_categories).toList()
        val selectedCategory = viewModel.selectedCategory
        val showError = viewModel.showError
        val maxLength = 30
        val maxLengthAddress = 100

        val isLoading = viewModel.isLoading

        //clear fields when the screen is disposed
        DisposableEffect(Unit) {
            onDispose {
                viewModel.clearFields()
            }
        }
        //collect error message and update the UI
        LaunchedEffect(Unit) {
            viewModel.showErrorMessage.collect { errorMessage ->
                if (errorMessage) {
                    Toast.makeText(context, R.string.error_address, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.foodtruck_added, Toast.LENGTH_SHORT).show()
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = stringResource(R.string.add_foodtruck),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = truckName,
            onValueChange = viewModel::onTruckNameChange,
            label = stringResource(id = R.string.name_max_characters, maxLength),
            errorMessage = if (truckName.isEmpty() && showError) stringResource(R.string.name_field) else null,
            maxLength = maxLength,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuWithFocus(
            selectedCategory = selectedCategory,
            categories = categories,
            onCategorySelected = viewModel::onCategorySelected,
            errorMessage = if (selectedCategory.isEmpty() && showError) stringResource(R.string.error_selected_category) else null
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = truckAddress,
            onValueChange = viewModel::onTruckAddressChange,
            label = stringResource(id = R.string.address),
            errorMessage = when {
                truckAddress.isEmpty() && showError -> stringResource(R.string.check_address)
                truckAddress.length >= maxLengthAddress -> stringResource(
                    R.string.max_characters_address,
                    maxLengthAddress
                )

                else -> null
            },
            maxLength = maxLengthAddress,
            singleLine = false,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        SubmitButton(
            onClick = {
                viewModel.addFoodTruckToFirestore(context)
            },
            isLoading = isLoading
        )
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit, isLoading: Boolean = false) {
    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.teal_700),
                contentColor = Color.White,
                disabledContainerColor = colorResource(id = R.color.teal_700),
                disabledContentColor = Color.White
            )

        ) {
            Text(text = stringResource(R.string.add))
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp))
    }
}


@Preview
@Composable
fun InformationScreenPreview() {
    InformationScreen()
}