package com.rma.catapult.catImages.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CatPhotoDao {
    @Upsert
    fun upsertAll(catPhotos: List<CatPhoto>)

    @Query("SELECT * FROM CatPhoto WHERE CatPhoto.ownerId = :catId")
    fun observeCatPhotos(catId: String): Flow<List<CatPhoto>>
}