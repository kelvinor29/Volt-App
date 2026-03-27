package com.voltfitness.app.domain.usecase.exercise

import com.voltfitness.app.domain.repository.ExerciseRepository
import javax.inject.Inject

/**
 * Refreshes the local exercise catalog from the remote API.
 * Safe to call multiple times; it will simply update the local cache.
 */
class RefreshExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke() {
        exerciseRepository.refreshExercisesFromRemote()
    }
}
