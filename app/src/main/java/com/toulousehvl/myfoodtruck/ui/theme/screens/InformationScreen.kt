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
import com.toulousehvl.myfoodtruck.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var truckName by remember { mutableStateOf("") }
        var truckAddress by remember { mutableStateOf("") }
        var truckCategory by remember { mutableStateOf("") }
        var showError by remember { mutableStateOf(false) }
        val maxLength = 30
        val maxLengthAddress = 100

        var isLoading by remember { mutableStateOf(false) }
        val context = LocalContext.current
        var result by remember { mutableStateOf<String?>(null) }

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

        DropdownMenuWithFocus()

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
                if (truckName.isEmpty() || truckAddress.isEmpty() || (truckCategory.length >= maxLengthAddress)) {
                    showError = true
                } else {
                    showError = false
                    isLoading = true
                    // Lancer la recherche d'adresse dans une coroutine
                    result = getLatLngFromAddress(context, truckAddress)
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
fun SubmitButton(onClick: () -> Unit, isLoading: Boolean, result: String?) {
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

    result?.let {
        Text(text = it)
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

fun getLatLngFromAddress(context: Context, mAddress: String): String {
    val coder = Geocoder(context)
    lateinit var address: List<Address>
    return try {
        address = coder.getFromLocationName(mAddress, 5) as List<Address>
        if (address.isEmpty()) {
            "Fail to find Lat,Lng"
        } else {
            val location = address[0]
            " Latitude: ${location.latitude}\n Longitude: ${location.longitude}"
        }
    } catch (e: Exception) {
        return "Fail to find Lat,Lng"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithFocus() {
    val categories = LocalContext.current.resources.getStringArray(R.array.food_categories).toList()
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(categories[0]) }

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
            value = selectedOption,
            onValueChange = { selectedOption = it },
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
                        selectedOption = category
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