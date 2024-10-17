package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var truckName by remember { mutableStateOf(TextFieldValue()) }
        var truckAddress by remember { mutableStateOf(TextFieldValue()) }
        var truckCategory by remember { mutableStateOf(TextFieldValue()) }

        Text(text = "Information")
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = truckName,
            onValueChange = { truckName = it },
            label = { Text(text = "Nom") },
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuWithFocus()

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = truckAddress,
            onValueChange = { truckAddress = it },
            label = { Text(text = "Adresse") },
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {},
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Ajouter")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithFocus() {
    val categories = listOf("Italien/Pizza", "Thaï", "Asiatique", "Africain", "Kebab", "Burger", "Autre")
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
            label = { Text("Choisir une catégorie") },
            trailingIcon = {
                Icon(icon, "", Modifier.clickable { expanded = !expanded })
            },
            //need this line to show the dropdown menu
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                Log.d("DropdownMenuWithFocus", "=== DropdownMenu category list: $category")
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