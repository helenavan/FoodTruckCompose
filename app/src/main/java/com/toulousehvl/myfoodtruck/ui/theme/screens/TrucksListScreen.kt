package com.toulousehvl.myfoodtruck.ui.theme.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.toulousehvl.myfoodtruck.R
import com.toulousehvl.myfoodtruck.data.ResultWrapper
import com.toulousehvl.myfoodtruck.data.model.Truck
import com.toulousehvl.myfoodtruck.data.utils.CategoriesUtils.Companion.setTruckCategory
import com.toulousehvl.myfoodtruck.navigation.NavigationItem
import com.toulousehvl.myfoodtruck.ui.theme.composables.SearchBar


@ExperimentalMaterialApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrucksListScreen(
    navController: NavHostController,
    viewModel: TrucksListViewModel = hiltViewModel()
) {
    val trucks by viewModel.dataListTrucksState.collectAsStateWithLifecycle()
    val uiState by viewModel.loaderUiState.collectAsStateWithLifecycle()
    val searchText = viewModel.searchtext
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    //animation
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState is ResultWrapper.Loading && trucks.isNotEmpty(),
        onRefresh = viewModel::fetchDataFromFirestore
    )

    Column {
        Spacer(modifier = Modifier.height(56.dp))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(16.dp))
                SearchBar(
                    value = searchText,
                    hint = stringResource(R.string.search_foodtruck),
                    onValueChange = viewModel::onSearchTextChange
                )
                Spacer(modifier = Modifier.height(16.dp))
                //TODO
                if (searchResults.isNullOrEmpty()) {
                    Text(
                        text = stringResource(R.string.empty_truck), modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = Color.Gray
                    )
                }

                TruckList(
                    trucks = if (searchText.isEmpty()) trucks else searchResults,
                    onItemClick = { selectedTruck ->
                        navController.navigate(
                            NavigationItem.MapTruck.route
                                .replace("{documentId}", "${selectedTruck.documentId}")
                        )
                    }
                )
            }

            PullRefreshIndicator(
                refreshing = ResultWrapper.Loading(true) == uiState,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = colorResource(id = R.color.teal_700),
                contentColor = Color.White
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun TruckList(
    trucks: List<Truck>,
    onItemClick: (Truck) -> Unit,
) {
    if (trucks.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(items = trucks) { truck ->
                TruckItem(truck = truck, onItemClick = { clicked -> onItemClick(clicked) })
            }
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.padding(4.dp, 2.dp, 2.dp, 4.dp),
                painter = painterResource(id = setTruckCategory(truck.categorie.toString())),
                contentDescription = "frites"
            )

            Column {
                Text(
                    modifier = Modifier.padding(8.dp, 2.dp, 4.dp),
                    text = "${truck.nameTruck}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(8.dp, 2.dp, 4.dp),
                    text = "${truck.categorie}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier.padding(8.dp, 2.dp, 4.dp, 4.dp),
                    text = "${truck.num ?: ""} ${truck.street ?: ""} ${truck.zipCode ?: ""} ${truck.city ?: ""}",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
            }

        }
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