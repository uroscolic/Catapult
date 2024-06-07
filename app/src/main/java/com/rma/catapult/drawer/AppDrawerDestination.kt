package com.rma.catapult.drawer

sealed class AppDrawerDestination {
    data object Profile : AppDrawerDestination()
    data object Leaderboard : AppDrawerDestination()
    data object EditProfile: AppDrawerDestination()


}