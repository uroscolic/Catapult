package com.rma.catapult.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rma.catapult.details.details
import com.rma.catapult.list.catList

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "allCats",
    ) {
        catList(route = "allCats", navController = navController)
        details(route = "details/{id}", navController = navController)

    }

}

