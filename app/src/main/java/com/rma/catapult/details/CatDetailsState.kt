package com.rma.catapult.details

import com.rma.catapult.domain.CatInfo

data class CatDetailsState(
    val loading : Boolean = false,
    val cat : CatInfo? = null,
    val catId : String,
    val error : Throwable? = null
)
