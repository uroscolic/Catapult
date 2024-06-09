package com.rma.catapult.leaderboard.api.model

data class LeaderboardUiModel (

    val id : Int = 0,
    val nickname : String = "",
    val result : Double = 0.0,
    val category : Int = 0,
    val numberOfQuizzesPlayed : Int = 0,
)