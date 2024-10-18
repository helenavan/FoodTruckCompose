package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.data.model.Truck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(viewModel: InformationViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var truckName by remember { mutableStateOf("") }
        var truckAddress by remember { mutableStateOf("") }
        val categories =
            LocalContext.current.resources.getStringArray(R.array.food_categories).toList()
        var selectedCategory by remember { mutableStateOf(categories[0]) }
        var showError by remember { mutableStateOf(false) }
        val maxLength = 30
        val maxLengthAddress = 100

        var isLoading by remember { mutableStateOf(false) }
        val context = LocalContext.current
        var result by remember { mutableStateOf<Address?>(null) }

        Text(text = stringResource(R.string.ajouter_un_foodtruck))
        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = truckName,
            onValueChange = { truckName = it },
            label = stringResource(id = R.string.nom_max_caract_res, maxLength),
            errorMessage = if (truckName.isEmpty() && showError) stringResource(R.string.veuillez_entrer_un_nom) else null,
            maxLength = maxLength,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuWithFocus(
            selectedCategory = selectedCategory,
            categories = categories,
            onCategorySelected = { selectedCategory = it })

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = truckAddress,
            onValueChange = { truckAddress = it },
            label = stringResource(id = R.string.adresse),
            errorMessage = when {
                truckAddress.isEmpty() && showError -> stringResource(R.string.veuillez_entrer_une_adresse_valide)
                truckAddress.length >= maxLengthAddress -> stringResource(
                    R.string.adresse_max_characteres,
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
                if (truckName.isEmpty() || truckAddress.isEmpty()) {
                    showError = true
                } else {
                    showError = false
                    isLoading = true
                    // Lancer la recherche d'adresse dans une coroutine
                    result = getLatLngFromAddress(context, truckAddress)
                    viewModel.addDataToFirestore(
                        Truck(
                            nameTruck = truckName,
                            categorie = selectedCategory,
                            latd = result?.latitude,
                            lgtd = result?.longitude,
                            date = System.currentTimeMillis(),
                            adresse = truckAddress,
                            zipCode = result?.postalCode,
                            city = result?.locality,
                            country = result?.countryName,
                            street = result?.thoroughfare
                        )
                    )
                    isLoading = false
                    Log.d("result", "===" + result.toString())
                }
            },
            isLoading = isLoading,
            result = result
        )
    }
}

@Composable
fun SubmitButton(onClick: () -> Unit, isLoading: Boolean, result: Address?) {
    Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.ajouter))
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(20.dp))
    }
}

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
        modifier = Modifier.fillMaxWidth(),
        maxLines = maxLines ?: 1
    )

    errorMessage?.let {
        Text(
            text = it,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun getLatLngFromAddress(context: Context, mAddress: String): Address? {
    val coder = Geocoder(context)
    return try {
       val addressList: List<Address> = coder.getFromLocationName(mAddress, 5) as List<Address>
        if (addressList.isEmpty()) {
            null
        } else {
            addressList[0]
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithFocus(
    selectedCategory: String,
    categories: List<String>,
    onCategorySelected: (String) -> Unit
) {
    // val categories = LocalContext.current.resources.getStringArray(R.array.food_categories).toList()
    var expanded by remember { mutableStateOf(false) }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    // Initialisation du FocusRequester
    val focusRequester = remember { FocusRequester() }

    // Utilisez LaunchedEffect pour demander le focus après la composition
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.focusRequester(focusRequester)
    ) {
        OutlinedTextField(
            value = selectedCategory,
            // onValueChange = { selectedCategory = it },
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.choisir_une_cat_gorie)) },
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded })
            },
            //need this line to show the dropdown menu
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview
@Composable
fun InformationScreenPreview() {
    InformationScreen()
}