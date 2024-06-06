package com.rma.catapult.cat.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.rma.catapult.cat.domain.CatImage
import com.rma.catapult.cat.domain.Weight

class Converter {
    @TypeConverter
    fun fromWeight(weight: Weight): String {
        return Gson().toJson(weight)
    }

    @TypeConverter
    fun toWeight(weightString: String): Weight {
        return Gson().fromJson(weightString, Weight::class.java)
    }

    @TypeConverter
    fun fromCatImage(catImage: CatImage): String {
        return Gson().toJson(catImage)
    }

    @TypeConverter
    fun toCatImage(catImageString: String): CatImage {
        return Gson().fromJson(catImageString, CatImage::class.java)
    }
}