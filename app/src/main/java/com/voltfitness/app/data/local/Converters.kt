package com.voltfitness.app.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { gson.toJson(it) }
//        return gson.toJson(value) TODO: Check what is better
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value.isNullOrEmpty()) return emptyList()

        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}