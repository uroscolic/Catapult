package com.rma.catapult.catImages.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CatPhoto (
    @PrimaryKey val id: String,
    val ownerId: String,
    val url: String,
    val width: Int,
    val height: Int,
)