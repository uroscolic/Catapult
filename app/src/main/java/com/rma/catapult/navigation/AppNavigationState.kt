package com.rma.catapult.navigation

import com.rma.catapult.leaderboard.api.model.LeaderboardUiModel
import com.rma.catapult.user.register.UserRegisterUiEvent

data class AppNavigationState (
    val registered: Boolean = false,
)