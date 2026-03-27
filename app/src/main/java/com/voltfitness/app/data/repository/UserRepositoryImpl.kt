package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.UserDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getCurrentUser(): Flow<User?> =
        userDao.getCurrentUserFlow().map { entity ->
            entity?.toDomain()
        }

    override suspend fun getUserById(id: Long): User? =
        userDao.getUserById(id)?.toDomain()

    override suspend fun getCurrentUserId(): Long? =
        userDao.getCurrentUserId()

    override suspend fun upsertUser(user: User): Long =
        userDao.upsertUser(user.toEntity())

    override suspend fun deleteUser(user: User) {
        userDao.getUserById(user.id)?.let { entity ->
            userDao.deleteUser(entity)
        }
    }

    override suspend fun getUserCount(): Int = userDao.getUserCount()
}