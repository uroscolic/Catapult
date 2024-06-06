package com.rma.catapult.cat.repository


import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.db.CatWithImages
import com.rma.catapult.cat.domain.CatInfo
import com.rma.catapult.cat.domain.CatImage
import com.rma.catapult.cat.list.api.CatApi
import com.rma.catapult.cat.mapper.asCatDbModel
import com.rma.catapult.catImages.mappers.asCatPhotoDbModel
import com.rma.catapult.db.CatapultDatabase
import com.rma.catapult.networking.retrofit


object Repository {

    private var mutableData = SampleData.toMutableList()
    private val catApi : CatApi = retrofit.create(CatApi::class.java)
    private val database by lazy { CatapultDatabase.database }

    fun allData() : List<CatInfo> = mutableData

    suspend fun fetchAllCats() {
        val allCats = catApi.getAllCats()
        database.catDao().insertAll(cats = allCats.map{it.asCatDbModel()})
    }
    suspend fun getCatWithImages(catId: String): CatWithImages? {
        return database.catDao().getCatWithImages(id = catId)
    }
    fun observeAllCats() = database.catDao().observeAll()

    suspend fun fetchCatImages(id: String) : List<CatImage> {
        val images = catApi.getCatImages(id = id)
        for(image in images){
            image.ownerId = id
        }
        database.catPhotoDao().upsertAll(images.map{it.asCatPhotoDbModel()})
        return images
    }
    fun fetchCatById(id: String) : Cat? {
        return getById(id)
    }

    fun getById(id: String) : Cat? {
        return database.catDao().getById(id)
    }

}