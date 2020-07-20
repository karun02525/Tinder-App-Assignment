package com.dkatalis.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dkatalis.model.User
import com.dkatalis.room.converter.LocationConverter
import com.dkatalis.room.converter.NameConverter
import com.dkatalis.room.dao.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
@TypeConverters(value = [LocationConverter::class, NameConverter::class])
abstract class RoomDb : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDb? = null

        fun getDatabase(context: Context): RoomDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDb::class.java,
                    "dkatalisDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}