package com.rma.catapult.cat.db

import androidx.room.Embedded
import androidx.room.Relation
import com.rma.catapult.catImages.db.CatPhoto

data class CatWithImages (

    @Embedded
    val cat: Cat,

    @Relation(
        parentColumn = "id",
        entityColumn = "ownerId"
    )

    val images: List<CatPhoto>
)