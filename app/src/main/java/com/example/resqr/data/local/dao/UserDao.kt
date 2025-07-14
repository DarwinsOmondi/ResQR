package com.example.resqr.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.resqr.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

    @Query("SELECT * FROM user_description")
    fun getUser(): UserEntity

    @Query("DELETE FROM user_description")
    fun deleteAll()

    @Query("UPDATE user_description SET description = :description WHERE id = :userId")
    fun update(userId: String, description: String)

}