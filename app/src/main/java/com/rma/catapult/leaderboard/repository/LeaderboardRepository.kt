package com.rma.catapult.leaderboard.repository

import android.util.Log
import com.rma.catapult.leaderboard.api.LeaderboardApi
import com.rma.catapult.leaderboard.model.Leaderboard
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi
) {
    companion object {
        var position = 1
    }
    private var _results = mutableListOf<Leaderboard>()
    suspend fun fetchAllResults(): List<Leaderboard> {
        val results = leaderboardApi.getResults()
        for(result in results) {
            result.id = position++

        }
        _results = results.toMutableList()
        for (result in results) {
            result.totalTimesPlayed = countResultsForUser(result.nickname)
        }
        position = 1
        return results
    }
     private fun countResultsForUser(nickname: String): Int {
        val results = _results
        return results.filter { it.nickname == nickname }.size
     }
}