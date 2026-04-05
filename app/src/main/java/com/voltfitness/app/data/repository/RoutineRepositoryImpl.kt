package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.RoutineDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.model.RoutineExercise
import com.voltfitness.app.domain.relations.RoutineWithDaysDomain
import com.voltfitness.app.domain.relations.RoutineWithFullDaysDomain
import com.voltfitness.app.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [RoutineRepository] managing the lifecycle of complex workout routines.
 * Handles cascading operations and hierarchical data retrieval for routines, days, and exercises.
 */
class RoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao
) : RoutineRepository {

    // ==================== ROUTINES ====================

    /**
     * Observes a complete routine hierarchy including nested days, exercises, and sets.
     */
    override fun getRoutineWithFullDays(routineId: Long): Flow<RoutineWithFullDaysDomain?> =
        routineDao.getRoutineWithFullDaysFlow(routineId).map { relation ->
            relation?.toDomain()
        }

    /**
     * Retrieves basic routine metadata.
     */
    override suspend fun getRoutineById(routineId: Long): Routine? =
        routineDao.getRoutineById(routineId)?.toDomain()

    /**
     * Persists routine metadata and triggers necessary maintenance tasks (like timestamp updates).
     * @return The canonical routine ID.
     */
    override suspend fun upsertRoutine(routine: Routine): Long =
        routineDao.upsertRoutineWithMaintenance(routine.toEntity())

    /**
     * Removes a routine. Dependencies are managed via database CASCADE constraints.
     */
    override suspend fun deleteRoutine(routineId: Long) =
        routineDao.deleteRoutineById(routineId)

    // ==================== ROUTINE DAYS ====================

    /**
     * Observes a routine and its associated days, ordered by [RoutineDay.dayOrder].
     */
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
     * Persists a single routine day.
     */
    override suspend fun upsertRoutineDay(day: RoutineDay): Long =
        routineDao.upsertRoutineDay(day.toEntity())

    /**
     * Persists multiple days, maintaining the input order for the returned IDs.
     */
    override suspend fun upsertRoutineDays(days: List<RoutineDay>): List<Long> =
        routineDao.upsertRoutineDays(days.map { it.toEntity() })

    /**
     * Cleans up days that are no longer part of the routine's current configuration.
     * Used during routine editing to sync local state with user changes.
     */
    override suspend fun deleteOrphanDays(routineId: Long, keepDayIds: List<Long>) =
        routineDao.deleteOrphanDays(routineId, keepDayIds)

    /**
     * Removes all days associated with a specific routine.
     */
    override suspend fun deleteRoutineDaysByRoutineId(routineId: Long) =
        routineDao.deleteRoutineDaysByRoutineId(routineId)

    // ==================== ROUTINE EXERCISES ====================

    /**
     * Persists exercises assigned to routine days.
     *
     * WARNING: Currently returns IDs from the domain model. If the domain model
     * uses temporary IDs (e.g., 0), this will not return the database-generated IDs.
     */
    override suspend fun upsertRoutineExercises(exercises: List<RoutineExercise>): List<Long> {
        routineDao.upsertRoutineExercises(exercises.map { it.toEntity() })
        return exercises.map { it.routineExerciseId }
    }

    /**
     * Fetches all exercises for a specific day in their designated order.
     */
    override suspend fun getExercisesByDayId(dayId: Long): List<RoutineExercise> =
        routineDao.getExercisesByDayId(dayId).map { it.toDomain() }
}