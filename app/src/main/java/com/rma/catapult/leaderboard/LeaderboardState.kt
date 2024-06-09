package com.rma.catapult.leaderboard

import com.rma.catapult.leaderboard.api.model.LeaderboardUiModel

data class LeaderboardState (
    val loading: Boolean = false,
    val error: Throwable? = null,
    val results: List<LeaderboardUiModel> = emptyList()
)