package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.voltfitness.app.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity): Long

    @Query("SELECT * FROM users ORDER BY createdAt ASC LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserEntity?> // observeFirstUser

    @Query("SELECT * FROM users ORDER BY createdAt ASC LIMIT 1")
    suspend fun getCurrentUser(): UserEntity? // getFirstUser

    @Query("SELECT userId FROM users LIMIT 1")
    suspend fun getCurrentUserId(): Long?

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users LIMIT 1")
    suspend fun getUserCount(): Int

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

}