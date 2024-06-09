package com.rma.catapult.leaderboard.api

import com.rma.catapult.cat.domain.CatInfo
import com.rma.catapult.leaderboard.model.Leaderboard
import retrofit2.http.GET
import retrofit2.http.POST

interface LeaderboardApi {

    @GET("leaderboard?category=2") //TODO treba 3
    suspend fun getResults(): List<Leaderboard>

    @POST("leaderboard")
    suspend fun postResult(leaderboard: Leaderboard)
}