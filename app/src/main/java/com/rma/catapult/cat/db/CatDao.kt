package com.rma.catapult.cat.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cats: List<Cat>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cat: Cat)


    @Query("SELECT * FROM Cat")
    suspend fun getAll(): List<Cat>

    @Transaction
    @Query("SELECT * FROM Cat WHERE id = :id")
    suspend fun getCatWithImages(id: String): CatWithImages?

    @Query("SELECT * FROM Cat")
    fun observeAll(): Flow<List<Cat>>

    @Query("SELECT * FROM Cat WHERE id = :id")
    fun getById(id: String): Cat?

}