package com.voltfitness.app.domain.repository

import com.voltfitness.app.data.local.entities.UserEntity
import com.voltfitness.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getCurrentUser(): Flow<User?>

    suspend fun getUserById(id: Long): User?

    suspend fun getCurrentUserId(): Long?

    suspend fun upsertUser(user: User): Long

    suspend fun deleteUser(user: User)

    suspend fun getUserCount(): Int

}

