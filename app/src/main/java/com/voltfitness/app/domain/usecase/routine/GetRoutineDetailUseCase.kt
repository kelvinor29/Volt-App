package com.voltfitness.app.domain.usecase.routine

import com.voltfitness.app.domain.relations.RoutineWithDaysDomain
import com.voltfitness.app.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observes a routine with all its training days for the editor screen.
 * Returns a reactive [Flow] so the UI stays updated if data changes
 * from another source (e.g., sync).
 */
class GetRoutineDetailUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    operator fun invoke(routineId: Long): Flow<RoutineWithDaysDomain?> =
        routineRepository.getRoutineWithDays(routineId)
}
