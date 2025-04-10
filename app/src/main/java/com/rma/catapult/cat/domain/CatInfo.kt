package com.rma.catapult.cat.domain

import kotlinx.serialization.Serializable

@Serializable
data class CatInfo(
    val id: String,
    val name: String,
    val alt_names: String = "",
    val description: String,
    val origin: String,
    val temperament: String,
    val life_span: String,
    val weight: Weight,
    val adaptability: Int,
    val affection_level: Int,
    val child_friendly: Int,
    val dog_friendly: Int,
    val energy_level: Int,
    val rare: Int,
    val wikipedia_url: String = "",
    val image: CatImage = CatImage(id,"", "", 0, 0),
    var avg_weight: Double = 0.0,
    var avg_life_span: Double = 0.0
)
@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)
@Serializable
data class CatImage(
    var ownerId: String = "",
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)