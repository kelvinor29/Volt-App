package com.voltfitness.app.data.local.dao

import androidx.room.*
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.data.local.relations.RoutineWithFullDays
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import kotlin.math.log

@Dao
interface RoutineDao {

    @Upsert
    suspend fun upsertRoutine(routine: RoutineEntity): Long

    @Transaction
    suspend fun saveFullRoutineAtomic(
        routine: RoutineEntity,
        daysWithExercises: List<Pair<RoutineDayEntity, List<RoutineExerciseEntity>>>
    ): Long {
        val upsertResult = upsertRoutine(routine)
        val routineId = if (upsertResult == -1L) routine.routineId else upsertResult

        if (routine.isActive) deactivateAllRoutines(routineId)

        val generatedDayIds = mutableListOf<Long>()

        daysWithExercises.forEach { (day, exercises) ->
            val dayUpsertResult = upsertRoutineDay(day.copy(routineId = routineId))
            val dayId = if (dayUpsertResult == -1L) day.dayId else dayUpsertResult

            generatedDayIds.add(dayId)

            if (exercises.isNotEmpty()) {
                val exercisesWithCorrectIds = exercises.map {
                    it.copy(
                        routineId = routineId,
                        dayId = dayId
                    )
                }
                upsertRoutineExercises(exercisesWithCorrectIds)
            }
        }

        deleteOrphanDays(routineId, generatedDayIds)

        return routineId
    }

    /**
     * Deactivates all routines.
     */
    @Query("UPDATE routines SET is_active = 0 WHERE routineId != :excludedId")
    suspend fun deactivateAllRoutines(excludedId: Long)

    /**
     * Retrieves all training days belonging to the currently active routine.
     * Uses a JOIN so no second query is needed from the ViewModel layer.
     *
     * @return A continuous [Flow] that emits whenever the active routine or its days change.
     */
    @Query(
        """
    SELECT rd.*
    FROM routine_days rd
    INNER JOIN routines r ON r.routineId = rd.routineId
    WHERE r.is_active = 1
    ORDER BY rd.dayOrder ASC
    """
    )
    fun getActivRoutineDays(): Flow<List<RoutineDayEntity>>

    /**
     * Retrieves a single routine by ID.
     */
    @Query("SELECT * FROM routines WHERE routineId = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity?

    /**
     * Retrieves a single routine with its full days and exercises, by ID.
     */
    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    fun getRoutineWithFullDaysFlow(routineId: Long): Flow<RoutineWithFullDays?>

    /**
     * Delete days associated with a routine, by ID.
     */
    @Query("DELETE FROM routine_days WHERE routineId = :routineId")
    suspend fun deleteRoutineDaysByRoutineId(routineId: Long)

    /**
     * Removes days that are no longer part of the routine to prevent orphan records.
     */
    @Query("DELETE FROM routine_days WHERE routineId = :routineId AND dayId NOT IN (:keepDayIds)")
    suspend fun deleteOrphanDays(routineId: Long, keepDayIds: List<Long>)

    @Upsert
    suspend fun upsertRoutineExercises(exercises: List<RoutineExerciseEntity>)

    /**
     * Delete a routine by ID.
     */
    @Query("DELETE FROM routines WHERE routineId = :routineId")
    suspend fun deleteRoutineById(routineId: Long)

    @Upsert
    suspend fun upsertRoutineDay(day: RoutineDayEntity): Long

    @Upsert
    suspend fun upsertRoutineDays(days: List<RoutineDayEntity>): List<Long>

    @Query(
        """
        SELECT * FROM routine_exercises
        WHERE dayId = :dayId
        ORDER BY orderInDay ASC
        """
    )
    suspend fun getExercisesByDayId(dayId: Long): List<RoutineExerciseEntity>
}