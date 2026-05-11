package com.voltfitness.app.domain.usecase.routine

import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.model.RoutineExercise
import com.voltfitness.app.domain.repository.RoutineRepository
import javax.inject.Inject

/**
 * Persists a routine, its training days, and every exercise per day atomically.
 *
 * ## Strategy
 * 1. **Upsert routine** → obtains the canonical `routineId` (new or existing).
 * 2. **Upsert days** → obtains the canonical `dayId` list for each day.
 *    Uses upsert (not delete+reinsert) so existing day IDs are preserved,
 *    which keeps [RoutineExercise.dayId] foreign keys stable.
 * 3. **Delete orphan days** that are no longer in the list.
 * 4. **Build exercises** with the real `routineId` and `dayId` resolved in
 *    steps 1–2, then upsert them.
 *
 * This avoids the bug where `routine.id == 0L` or `dayUi.id == 0L` are used
 * as foreign keys before the database has generated the real IDs.
 *
 * @param routine  Routine metadata. `id == 0L` signals a new record.
 * @param daysWithExercises Each [RoutineDay] paired with its [RoutineExercise] list.
 * @return The persisted routine ID.
 */
class SaveRoutineUseCase @Inject constructor(
    private val routineRepository: RoutineRepository
) {
    suspend operator fun invoke(
        routine: Routine,
        daysWithExercises: List<Pair<RoutineDay, List<RoutineExercise>>>
    ): Long {
        return routineRepository.saveFullRoutine(routine, daysWithExercises)
    }
}
