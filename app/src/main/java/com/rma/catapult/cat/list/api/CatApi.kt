package com.rma.catapult.cat.list.api

import com.rma.catapult.cat.domain.CatInfo
import com.rma.catapult.cat.domain.CatImage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatApi {
    @GET("breeds")
    suspend fun getAllCats(): List<CatInfo>

    @GET("images/search")
    suspend fun getCatImages(
        @Query("limit") limit : Int = 20,
        @Query("breed_ids") id : String,

    ): List<CatImage>
}