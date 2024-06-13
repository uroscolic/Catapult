package com.rma.catapult.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardResponse(
    val result: Result = Result(0, "", 0.0, 0),
    val ranking: Int = 0
)
@Serializable
data class Result(
    val category: Int = 0,
    val nickname: String = "",
    val result: Double = 0.0,
    val createdAt: Long = 0
)