package com.rma.catapult.cat.mapper

import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.domain.CatImage
import com.rma.catapult.cat.domain.CatInfo
import com.rma.catapult.cat.domain.Weight

fun CatInfo.asCatDbModel(): Cat {
    return Cat(
        id = this.id,
        name = this.name,
        alt_names = this.alt_names,
        description = this.description,
        origin = this.origin,
        temperament = this.temperament,
        life_span = this.life_span,
        weight = Weight(
            imperial = this.weight.imperial,
            metric = this.weight.metric
        ),
        adaptability = this.adaptability,
        affection_level = this.affection_level,
        child_friendly = this.child_friendly,
        dog_friendly = this.dog_friendly,
        energy_level = this.energy_level,
        rare = this.rare,
        wikipedia_url = this.wikipedia_url,
        image = CatImage(
            ownerId = this.id,
            id = this.image.id,
            url = this.image.url,
            width = this.image.width,
            height = this.image.height
        ),
        avg_weight = this.avg_weight,
        avg_life_span = this.avg_life_span
    )
}