package com.rma.catapult.navigation

import android.util.Log
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rma.catapult.catImages.grid.catImageGrid
import com.rma.catapult.catImages.photoViewer.catPhotoViewer
import com.rma.catapult.cat.details.details
import com.rma.catapult.cat.list.catList
import com.rma.catapult.leaderboard.leaderboard
import com.rma.catapult.user.edit.editUser
import com.rma.catapult.user.register.register

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "register",
    ) {
        catList(
            route = "allCats",
            onProfileClick = {
                navController.navigate("profile")
            },
            onEditProfileClick = {
                navController.navigate("editProfile")
            },
            onLeaderboardClick = {
                navController.navigate("leaderboard")
            },
            onCatSelected = { cat ->
                navController.navigate("details/${cat.id}")
            }
        )
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
        register(route = "register",
            onRegisterClick = {
                navController.navigate("allCats")
            },
            alreadyRegistered = {
                navController.navigate("allCats")
            }
        )
        editUser(route = "editProfile",
            onEditClick = {
                navController.navigate("allCats")
            },
            onClose = {
                navController.navigateUp()
            }
        )
        leaderboard(route = "leaderboard")


    }

}

inline val SavedStateHandle.catId: String
    get() = checkNotNull(get("catId")) { "catId required" }

inline val SavedStateHandle.id: String
    get() = checkNotNull(get("id")) { "id required" }