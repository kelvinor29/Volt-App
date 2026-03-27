package com.voltfitness.app.domain.usecase.routine

import com.voltfitness.app.domain.repository.RoutineRepository
import javax.inject.Inject

/**
 * Deletes a routine and all its associated data (days, exercises, sets)
 * via database CASCADE constraints.
 */
class DeleteRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    suspend operator fun invoke(routineId: Long) =
        routineRepository.deleteRoutine(routineId)
}
