package com.voltfitness.app.domain.usecase.user

import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.usecase.home.EnsureDefaultFolderUseCase
import com.voltfitness.app.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Orchestrates the full registration process.
 * Ensures that a user is not created without their initial data structures (folders).
 */
class CompleteUserRegistrationUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val ensureDefaultFolderUseCase: EnsureDefaultFolderUseCase
) {
    suspend operator fun invoke(user: User): Result<Long> {
        return try {
            val userId = userRepository.upsertUser(user)

            ensureDefaultFolderUseCase(userId)

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}