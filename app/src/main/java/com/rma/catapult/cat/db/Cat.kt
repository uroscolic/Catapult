package com.rma.catapult.cat.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rma.catapult.cat.domain.CatImage
import com.rma.catapult.cat.domain.Weight

@Entity
data class Cat (

    @PrimaryKey val id: String,
    val name: String,
    val alt_names: String = "",
    val description: String,
    val origin: String,
    val temperament: String,
    val life_span: String,
    val weight: Weight = Weight(
        imperial = "",
        metric = ""
    ),
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val rare: Int,
    val wikipedia_url: String = "",
    val image : CatImage = CatImage(
        ownerId = id,
        id = "",
        url = "",
        width = 0,
        height = 0,
    ),
    var avg_weight: Double = 0.0,
    var avg_life_span: Double = 0.0
)