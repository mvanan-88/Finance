package com.mathi.finance.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class GridItemData(val title: String, val icon: ImageVector, val color: Color)

@Composable
fun HomeScreen(
    userEmail: String, onSignOut: () -> Unit, modifier: Modifier = Modifier
) {

//    val gridItems = listOf(
////        GridItemData("Bicycle", Icons.Default.DirectionsBike, Color(0xFFA2D398)),
////        GridItemData("Boat", Icons.Default.DirectionsBoat, Color(0xFF30CC2F)),
////        GridItemData("Bus", Icons.Default.DirectionsBus, Color(0xFFD793DB)),
////        GridItemData("Train", Icons.Default.Train, Color(0xFF285C27))
//    )
    Scaffold() {innerPadding->
        Column(
            modifier = Modifier.padding(top=innerPadding.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
//                items(gridItems) { item ->
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(100.dp)
//                            .background(item.color),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            verticalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                imageVector = item.icon,
//                                contentDescription = item.title,
//                                tint = Color.Black,
//                                modifier = Modifier.size(32.dp)
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = item.title,
//                                color = Color.Black,
//                                fontSize = 16.sp
//                            )
//                        }
//                    }
//                }
            }
        }
    }
}