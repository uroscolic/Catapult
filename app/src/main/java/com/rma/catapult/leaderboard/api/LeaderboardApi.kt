package com.rma.catapult.leaderboard.api


import com.rma.catapult.leaderboard.model.Leaderboard
import com.rma.catapult.leaderboard.model.LeaderboardPost
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LeaderboardApi {

    @GET("leaderboard?category=3")
    suspend fun getResults(): List<Leaderboard>

    @POST("leaderboard")
    suspend fun postResult(@Body leaderboardPost: LeaderboardPost)
}