package com.rma.catapult.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardPost(
    var nickname: String = "",
    val result: Double,
    val category: Int
)