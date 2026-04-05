package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.UserDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [UserRepository] that manages user profile data and
 * session state using [UserDao].
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    /**
     * Observes the currently authenticated user.
     * @return A [Flow] that emits the user profile or null if no session exists.
     */
    override fun getCurrentUser(): Flow<User?> =
        userDao.getCurrentUserFlow().map { entity ->
            entity?.toDomain()
        }

    /**
     * Retrieves a specific user by their unique identifier.
     */
    override suspend fun getUserById(id: Long): User? =
        userDao.getUserById(id)?.toDomain()

    /**
     * Synchronously fetches the ID of the current active user.
     * Useful for non-reactive data operations.
     */
    override suspend fun getCurrentUserId(): Long? =
        userDao.getCurrentUserId()

    /**
     * Persists or updates user profile information.
     * @return The row ID of the modified user record.
     */
    override suspend fun upsertUser(user: User): Long =
        userDao.upsertUser(user.toEntity())

    /**
     * Removes the user record from the database.
     */
    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toEntity())
    }

    /**
     * Returns the total number of registered users.
     * Primarily used to determine if the onboarding process is required.
     */
    override suspend fun getUserCount(): Int =
        userDao.getUserCount()
}