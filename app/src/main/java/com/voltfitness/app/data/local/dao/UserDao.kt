package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.voltfitness.app.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Upsert
    suspend fun upsertUser(user: UserEntity): Long

    /**
     * Observes the primary application user.
     */
    @Query("SELECT * FROM users ORDER BY createdAt ASC LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserEntity?>

    /**
     * Retrieves the primary application user.
     */
    @Query("SELECT * FROM users ORDER BY createdAt ASC LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT userId FROM users LIMIT 1")
    suspend fun getCurrentUserId(): Long?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Long): UserEntity?

}