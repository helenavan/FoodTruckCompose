package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.toulousehvl.myfoodtruck.MainViewModel
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.CategoryTruck
import com.toulousehvl.myfoodtruck.data.model.CategoryTruck.Companion.toCategoryTruckString
import com.toulousehvl.myfoodtruck.data.model.Truck
import com.toulousehvl.myfoodtruck.navigation.NavigationItem

@Composable
fun TrucksListScreen(navController: NavHostController) {
     val viewModel: MainViewModel = hiltViewModel()
    val trucks by viewModel.dataListTrucksState.collectAsStateWithLifecycle()
    val uiState by viewModel.foodTruckUserUiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ResultWrapper.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                TruckList(trucks = trucks, navController = navController, viewModel)
            }
        }
        is ResultWrapper.Error -> {
            Text(text = "Error fetching data")
        }
        is ResultWrapper.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun TruckList(
    trucks: List<Truck>,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(items = trucks) { truck ->
            TruckItem(truck = truck, onItemClick = { selectedTruck ->
                navController.navigate(
                    NavigationItem.MapTruck.route
                        .replace("{documentId}", "${selectedTruck.documentId}")
                )
                Log.d("TruckList", "selectedTruck ===> $selectedTruck")
            })
        }
    }
}

@Composable
fun TruckItem(truck: Truck, onItemClick: (Truck) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClick(truck) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.padding(4.dp, 2.dp, 2.dp, 4.dp),
                painter = painterResource(id = setTruckCategorie(truck.categorie.toString())),
                contentDescription = "frittes"
            )

            Column {
                Text(
                    modifier = Modifier.padding(4.dp, 2.dp, 4.dp, 4.dp),
                    text = "${truck.nameTruck}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.padding(4.dp, 2.dp, 4.dp, 4.dp),
                    text = "${truck.adresse}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

private fun setTruckCategorie(category: String): Int {
    return when (category) {
        CategoryTruck.Italian.toCategoryTruckString() -> R.drawable.ic_frittes
        CategoryTruck.Burger.toCategoryTruckString() -> R.drawable.ic_burger
        CategoryTruck.Asian.toCategoryTruckString() -> R.drawable.ic_thai
        CategoryTruck.Sushi.toCategoryTruckString() -> R.drawable.ic_sushi
        CategoryTruck.African.toCategoryTruckString() -> R.drawable.ic_africain
        CategoryTruck.Kebab.toCategoryTruckString() -> R.drawable.ic_kebab
        else -> R.drawable.ic_frittes
    }
}

@Preview
@Composable
fun TruckItemPreview() {
    TruckItem(
        truck = Truck(
            "1",
            "FrittesjjjfhfyrhjfkspsofofvnibviyvhbhiuhiuhiuhFrittesjjjfhfyrhjfkspsofofvnibviyvhbhiuhiuhiuh",
            "Thaï",
            1.0.toInt()
        ),
        onItemClick = {}
    )
}

//@Preview
//@Composable
//fun TruckListPreview() {
//    TruckList(
//        navController = NavController(LocalContext.current),
//        trucks = listOf(
//            Truck(),
//            Truck(),
//            Truck(),
//            Truck(),
//            Truck(),
//            Truck(),
//            Truck(),
//            Truck(),
//            Truck(),
//            Truck()
//        )
//    )
//}