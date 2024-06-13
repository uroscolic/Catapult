package com.rma.catapult.leaderboard

import com.rma.catapult.leaderboard.model.LeaderboardPost

sealed class LeaderboardUiEvent {
    data class ShareResult(val leaderboardPost: LeaderboardPost) : LeaderboardUiEvent()
    data class AddResultLocally(val leaderboardPost: LeaderboardPost) : LeaderboardUiEvent()

}