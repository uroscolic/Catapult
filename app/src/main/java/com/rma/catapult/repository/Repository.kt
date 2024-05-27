package com.rma.catapult.repository

import com.rma.catapult.domain.CatInfo
import com.rma.catapult.list.api.CatApi
import com.rma.catapult.networking.retrofit
import kotlinx.coroutines.delay


object Repository {

    private var mutableData = SampleData.toMutableList()
    private val catApi : CatApi = retrofit.create(CatApi::class.java)

    fun allData() : List<CatInfo> = mutableData

    suspend fun fetchAllCats() : List<CatInfo> {
        val allCats = catApi.getAllCats()
        mutableData = allCats.toMutableList()
        return allCats
    }
    suspend fun fetchCatById(id: String) : CatInfo? {
        delay(300)
        return getById(id)
    }

    fun getById(id: String) : CatInfo? {
        return mutableData.find { it.id == id }
    }
    fun search(search: String) : List<CatInfo> {
        if(search.isEmpty())
            return mutableData
        return mutableData.filter { it.name.contains(search, ignoreCase = true)
            || it.alt_names.contains(search, ignoreCase = true) }
    }

}