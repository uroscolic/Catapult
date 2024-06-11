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
//@Query("""
//    SELECT c1.*, c2.*
//    FROM Cat c1
//    JOIN Cat c2 ON c1.avg_weight != c2.avg_weight
//    ORDER BY RANDOM()
//    LIMIT 1
//""")
//    suspend fun getTwoRandomCatsWithDifferentWeights(): List<Cat>?
//    @Query("""
//    SELECT c1.*, c2.*
//    FROM Cat c1
//    JOIN Cat c2 ON c1.avg_life_span != c2.avg_life_span
//    ORDER BY RANDOM()
//    LIMIT 1
//""")
//    suspend fun getTwoRandomCatsWithDifferentLifeSpans(): List<Cat>?

}