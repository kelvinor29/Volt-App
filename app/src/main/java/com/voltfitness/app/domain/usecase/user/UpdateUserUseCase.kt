package com.voltfitness.app.domain.usecase.user

import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.repository.UserRepository
import javax.inject.Inject

/**
 * Persists changes to the user profile.
 * Called when the user edits personal data in the AddBodyComposition screen.
 */
class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Long> {
        if (user.name.isBlank())
            return Result.failure(IllegalArgumentException("Name cannot be empty"))

        userRepository.upsertUser(user)

        return Result.success(user.id)

    }
}
