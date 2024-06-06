package com.rma.catapult.catImages.mappers

import com.rma.catapult.cat.domain.CatImage
import com.rma.catapult.catImages.db.CatPhoto

fun CatImage.asCatPhotoDbModel(): CatPhoto {
    return CatPhoto(
        id = this.id,
        ownerId = this.ownerId,
        url = this.url,
        width = this.width,
        height = this.height,
    )
}