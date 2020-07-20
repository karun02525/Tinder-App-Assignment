package com.dkatalis.room.dao

import androidx.room.*
import com.dkatalis.model.User

@Dao
interface UserDao {

    @Query("SELECT * from User")
    fun getFavUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFav(user: User)
}