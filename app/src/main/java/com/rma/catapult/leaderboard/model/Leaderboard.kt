package com.rma.catapult.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class Leaderboard (
    var id : Int = 0,
    val nickname : String = "",
    val result : Double = 0.0,
    val category : Int = 0,
    var totalTimesPlayed : Int = 0
)