package com.voltfitness.app.domain.usecase.routine

import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.repository.RoutineRepository
import javax.inject.Inject

/**
 * Persists a routine along with its training days.
 *
 * For new routines (id == 0), it creates the routine first to obtain
 * the generated ID, then inserts all days referencing that ID.
 *
 * For existing routines, it performs a "delete-and-reinsert" strategy
 * for days to handle additions, removals, and reordering cleanly.
 * Exercise associations within days are NOT affected by this use case.
 *
 * @see RoutineRepository
 */
class SaveRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    /**
     * @param routine The routine metadata to save.
     * @param days The list of training days to associate with the routine.
     * @return The persisted routine ID.
     */
    suspend operator fun invoke(routine: Routine, days: List<RoutineDay>): Long {
        val routineId = routineRepository.upsertRoutine(routine)

        routineRepository.deleteRoutineDaysByRoutineId(routineId)
        routineRepository.upsertRoutineDays(
            days.map { it.copy(routineId = routineId) }
        )

        return routineId
    }
}
