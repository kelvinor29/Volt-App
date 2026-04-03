package com.voltfitness.app.domain.repository

import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.model.RoutineExercise
import com.voltfitness.app.domain.relations.RoutineDayFullDomain
import com.voltfitness.app.domain.relations.RoutineWithDaysDomain
import com.voltfitness.app.domain.relations.RoutineWithFullDaysDomain
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for Routine and RoutineDay CRUD operations.
 *
 * Folder-related queries have been moved to [FolderRepository]
 * to maintain single responsibility per aggregate.
 */
interface RoutineRepository {

    // ==================== ROUTINES ====================

    /** Observes a routine with all its training days and exercises. **/
    fun getRoutineWithFullDays(routineId: Long): Flow<RoutineWithFullDaysDomain?>

    /** Retrieves a single routine by ID (one-shot). */
    suspend fun getRoutineById(routineId: Long): Routine?

    /** Creates or updates a routine. Returns the routine ID. */
    suspend fun upsertRoutine(routine: Routine): Long

    /** Deletes a routine and all associated days/exercises (via CASCADE). */
    suspend fun deleteRoutine(routineId: Long)

    // ==================== ROUTINE DAYS ====================

    /** Observes a routine with all its training days. */
    suspend fun getRoutineWithDays(routineId: Long): Flow<RoutineWithDaysDomain?>

    /** Creates or updates a single training day. Returns the day ID. */
    suspend fun upsertRoutineDay(day: RoutineDay): Long

    /** Creates or updates multiple training days in batch. Returns their IDs. */
    suspend fun upsertRoutineDays(days: List<RoutineDay>): List<Long>

    /** Deletes all days for a routine (used before re-inserting updated list). */
    suspend fun deleteRoutineDaysByRoutineId(routineId: Long)

    // ==================== EXERCISES DAYS ====================

    suspend fun upsertRoutineExercises(exercises: List<RoutineExercise>): List<Long>

    suspend fun getExercisesByDayId(dayId: Long): List<RoutineExercise>

    suspend fun deleteOrphanDays(routineId: Long, keepDayIds: List<Long>)
}
