package com.rma.catapult.cat.details

import com.rma.catapult.cat.db.Cat

data class CatDetailsState(
    val loading : Boolean = false,
    val cat : Cat? = null,
    val catId : String,
    val error : Throwable? = null
)
