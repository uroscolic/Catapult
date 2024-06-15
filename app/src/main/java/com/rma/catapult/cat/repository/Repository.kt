package com.rma.catapult.cat.repository


import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.db.CatWithImages
import com.rma.catapult.cat.domain.CatInfo
import com.rma.catapult.cat.domain.CatImage
import com.rma.catapult.cat.list.api.CatApi
import com.rma.catapult.cat.mapper.asCatDbModel
import com.rma.catapult.catImages.db.CatPhoto
import com.rma.catapult.catImages.mappers.asCatPhotoDbModel
import com.rma.catapult.db.AppDatabase
import javax.inject.Inject


class Repository @Inject constructor(
    private val catApi : CatApi,
    private val database: AppDatabase,
) {
    var allCats = emptyList<CatInfo>()
    suspend fun fetchAllCats() {
        allCats = catApi.getAllCats()
        for(cat in allCats){
            cat.avg_weight = (cat.weight.metric.split(" ")[0].toDouble() + cat.weight.metric.split(" ")[2].toDouble()) / 2
            cat.avg_life_span = (cat.life_span.split(" ")[0].toDouble() + cat.life_span.split(" ")[2].toDouble()) / 2
        }

        database.catDao().insertAll(cats = allCats.map{it.asCatDbModel()})
        for(cat in allCats){
            fetchCatImagesIfNotInDatabase(cat.id)
        }

    }
    suspend fun getCatWithImages(catId: String): CatWithImages? {
        return database.catDao().getCatWithImages(id = catId)
    }
    fun observeAllCats() = database.catDao().observeAll()
    suspend fun fetchCatImagesIfNotInDatabase(id: String) {
        val existingImages = database.catPhotoDao().getCatPhotos(id)
        if (existingImages.isEmpty()) {
            fetchCatImages(id)
        }
    }
    suspend fun fetchCatImages(id: String){
        val images = catApi.getCatImages(id = id)
        for(image in images){
            image.ownerId = id
        }
        database.catPhotoDao().upsertAll(images.map{it.asCatPhotoDbModel()})

    }
    fun observeCatImages(catId: String) = database.catPhotoDao().observeCatPhotos(catId)
    fun fetchCatById(id: String) : Cat? {
        return getById(id)
    }

    private fun getById(id: String) : Cat? {
        return database.catDao().getById(id)
    }
    suspend fun getRandomCatPhoto(catId: String) : CatPhoto {
        return database.catPhotoDao().getRandomCatPhoto(catId)
    }


}