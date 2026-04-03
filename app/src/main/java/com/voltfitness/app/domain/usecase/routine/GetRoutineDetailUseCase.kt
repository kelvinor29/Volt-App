package com.voltfitness.app.domain.usecase.routine

import com.voltfitness.app.domain.relations.RoutineWithFullDaysDomain
import com.voltfitness.app.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observes a routine with all its training days **and** the full exercise list
 * for each day, ready for the editor screen.
 *
 * Returns a reactive [Flow] so the UI automatically reflects any change
 * that originates from another source (e.g., a background sync).
 *
 * @param routineRepository Source of truth for routine persistence.
 */
class GetRoutineDetailUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    operator fun invoke(routineId: Long): Flow<RoutineWithFullDaysDomain?> =
        routineRepository.getRoutineWithFullDays(routineId)
}
