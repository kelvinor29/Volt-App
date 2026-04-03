package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.RoutineDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.model.RoutineExercise
import com.voltfitness.app.domain.relations.RoutineDayFullDomain
import com.voltfitness.app.domain.relations.RoutineWithDaysDomain
import com.voltfitness.app.domain.relations.RoutineWithFullDaysDomain
import com.voltfitness.app.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Default implementation of [RoutineRepository].
 *
 * Responsibilities:
 * - Persist routine metadata.
 * - Persist routine days.
 * - Persist routine exercises assigned to each day.
 * - Expose reactive flows for:
 *   1. Full routine detail (routine -> days -> exercises -> sets)
 *   2. Single day detail (day -> exercises -> sets)
 *
 * Mapping from Room entities/relations to domain models is delegated
 * to dedicated mapper extensions.
 */
class RoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao
) : RoutineRepository {

    // ==================== ROUTINES ====================

    /**
     * Observes a full routine snapshot:
     * routine metadata + all days + all exercises + all sets.
     */
    override fun getRoutineWithFullDays(routineId: Long): Flow<RoutineWithFullDaysDomain?> =
        routineDao.getRoutineWithFullDaysFlow(routineId).map { relation ->
            relation?.toDomain()
        }

    /**
     * Retrieves a single routine entity by its ID.
     */
    override suspend fun getRoutineById(routineId: Long): Routine? =
        routineDao.getRoutineById(routineId)?.toDomain()

    /**
     * Creates or updates a routine and returns its canonical ID.
     */
    override suspend fun upsertRoutine(routine: Routine): Long =
        routineDao.upsertRoutineWithMaintenance(routine.toEntity())

    /**
     * Deletes a routine by ID.
     * Related days, exercises, and sets should be removed by CASCADE.
     */
    override suspend fun deleteRoutine(routineId: Long) =
        routineDao.deleteRoutineById(routineId)

    // ==================== ROUTINE DAYS ====================

    override suspend fun getRoutineWithDays(routineId: Long): Flow<RoutineWithDaysDomain?> =
        routineDao.getRoutineWithFullDaysFlow(routineId).map { relation ->
            relation?.let { full ->
                RoutineWithDaysDomain(
                    routine = full.routine.toDomain(),
                    days = full.days
                        .sortedBy { it.day.dayOrder }
                        .map { it.day.toDomain() }
                )
            }
        }

    /**
     * Creates or updates a single routine day and returns its canonical ID.
     */
    override suspend fun upsertRoutineDay(day: RoutineDay): Long =
        routineDao.upsertRoutineDay(day.toEntity())

    /**
     * Upserts a list of days and returns the canonical ID list
     * in the same order as the input list.
     */
    override suspend fun upsertRoutineDays(days: List<RoutineDay>): List<Long> =
        routineDao.upsertRoutineDays(days.map { it.toEntity() })

    /**
     * Deletes days that belong to the routine but are no longer present
     * in the editor payload.
     */
    override suspend fun deleteOrphanDays(routineId: Long, keepDayIds: List<Long>) =
        routineDao.deleteOrphanDays(routineId, keepDayIds)

    /**
     * Deletes all routine days for a given routine ID.
     */
    override suspend fun deleteRoutineDaysByRoutineId(routineId: Long) =
        routineDao.deleteRoutineDaysByRoutineId(routineId)

    // ==================== ROUTINE EXERCISES ====================

    /**
     * Upserts all exercises assigned to routine days.
     *
     * Note:
     * If your DAO currently returns Unit, this repository can only return
     * the existing IDs from the input models. If later you need the real
     * generated IDs for new rows, change the DAO to return List<Long>.
     */
    override suspend fun upsertRoutineExercises(exercises: List<RoutineExercise>): List<Long> {
        routineDao.upsertRoutineExercises(exercises.map { it.toEntity() })
        return exercises.map { it.routineExerciseId }
    }

    /**
     * Returns all exercises for a given day ordered by their position in the day.
     */
    override suspend fun getExercisesByDayId(dayId: Long): List<RoutineExercise> =
        routineDao.getExercisesByDayId(dayId).map { it.toDomain() }
}