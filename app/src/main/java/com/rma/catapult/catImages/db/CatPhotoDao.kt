package com.rma.catapult.catImages.db

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface CatPhotoDao {

    @Upsert
    fun upsertAll(catPhotos: List<CatPhoto>)
}