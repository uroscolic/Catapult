package com.rma.catapult.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rma.catapult.cat.converters.Converter
import com.rma.catapult.cat.db.Cat
import com.rma.catapult.cat.db.CatDao
import com.rma.catapult.catImages.db.CatPhoto
import com.rma.catapult.catImages.db.CatPhotoDao

@Database
    (
    entities = [
        Cat::class,
        CatPhoto::class
    ],
    version = 2,
    exportSchema = true,
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao
    abstract fun catPhotoDao(): CatPhotoDao

}