package com.dkatalis.room.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.dkatalis.model.Location

class LocationConverter {

    @TypeConverter
    fun objectToJson(value: Location?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToObject(value: String): Location? {
        return Gson().fromJson(value, Location::class.java) as Location
    }
}