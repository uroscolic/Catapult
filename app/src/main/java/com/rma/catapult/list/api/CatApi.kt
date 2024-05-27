package com.rma.catapult.list.api

import com.rma.catapult.domain.CatInfo
import retrofit2.http.GET

interface CatApi {
    @GET("breeds")
    suspend fun getAllCats(): List<CatInfo>

}