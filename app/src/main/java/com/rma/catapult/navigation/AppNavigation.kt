package com.rma.catapult.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rma.catapult.catImages.grid.catImageGrid
import com.rma.catapult.catImages.photoViewer.catPhotoViewer
import com.rma.catapult.cat.details.details
import com.rma.catapult.cat.list.catList

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "allCats",
    ) {
        catList(route = "allCats", navController = navController)
        details(route = "details/{id}", navController = navController)
        catImageGrid(route = "catImages/{catId}",
            arguments = listOf(navArgument("catId") {
                nullable = false
                type = NavType.StringType
            }),
            onImageClick = { catId, index ->
                navController.navigate("photos/$catId/$index")
            },
            onClose = {
                navController.navigateUp()
            }
        )
        catPhotoViewer(route = "photos/{catId}/{startIndex}",
            arguments = listOf(
                navArgument("catId") {
                    nullable = false
                    type = NavType.StringType
                },
                navArgument("startIndex") {
                    nullable = false
                    type = NavType.IntType
                }
            ),
            onClose = {
                navController.navigateUp()
            }
        )


    }

}

