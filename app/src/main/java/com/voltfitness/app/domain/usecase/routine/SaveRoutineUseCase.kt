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
        // Step 1 — persist the routine, get the real ID
        val routineId = routineRepository.upsertRoutine(routine)

        // Step 2 — upsert days bound to the real routineId, collect real dayIds
        val days = daysWithExercises.map { (day, _) -> day.copy(routineId = routineId) }
        val dayIds: List<Long> = routineRepository.upsertRoutineDays(days)

        // Step 3 — remove days that are no longer in the list (for edit mode)
        routineRepository.deleteOrphanDays(routineId, dayIds)

        // Step 4 — build exercises with real IDs and upsert them
        val exercises = daysWithExercises.flatMapIndexed { index, (_, exercisesForDay) ->
            val realDayId = dayIds[index]
            exercisesForDay.mapIndexed { order, exercise ->
                exercise.copy(
                    routineId = routineId,
                    dayId = realDayId
                )
            }
        }

        if (exercises.isNotEmpty()) {
            routineRepository.upsertRoutineExercises(exercises)
        }

        return routineId
    }
}
