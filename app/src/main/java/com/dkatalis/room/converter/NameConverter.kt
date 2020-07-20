package com.dkatalis.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.dkatalis.model.Name

class NameConverter {

    @TypeConverter
    fun objectToJson(value: Name?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToObject(value: String): Name? {
        return Gson().fromJson(value, Name::class.java) as Name
    }
}