package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.data.local.relations.RoutineWithFullDays
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    // ==================== ROUTINES ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutine(routine: RoutineEntity): Long

    @Query("UPDATE routines SET is_active = 0 WHERE folderId = :folderId")
    suspend fun resetMainRoutinesForFolderId(folderId: Long)

    @Transaction
    suspend fun upsertRoutineWithMaintenance(routine: RoutineEntity): Long {
        if (routine.isActive)
            resetMainRoutinesForFolderId(routine.folderId)
        return upsertRoutine(routine)
    }

    @Query("SELECT * FROM routines WHERE routineId = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity?

    /**
     * Full aggregate query for detail/editor flows.
     */
    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :routineId")
    fun getRoutineWithFullDaysFlow(routineId: Long): Flow<RoutineWithFullDays?>

    @Query("DELETE FROM routines WHERE routineId = :routineId")
    suspend fun deleteRoutineById(routineId: Long)

    // ==================== ROUTINE DAYS ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutineDay(day: RoutineDayEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutineDays(days: List<RoutineDayEntity>): List<Long>

    @Query("DELETE FROM routine_days WHERE routineId = :routineId")
    suspend fun deleteRoutineDaysByRoutineId(routineId: Long)

    @Query(
        """
        DELETE FROM routine_days
        WHERE routineId = :routineId
        AND dayId NOT IN (:keepDayIds)
        """
    )
    suspend fun deleteOrphanDays(routineId: Long, keepDayIds: List<Long>)

    // ==================== ROUTINE EXERCISES ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutineExercises(exercises: List<RoutineExerciseEntity>)

    @Query(
        """
        SELECT * FROM routine_exercises
        WHERE dayId = :dayId
        ORDER BY orderInDay ASC
        """
    )
    suspend fun getExercisesByDayId(dayId: Long): List<RoutineExerciseEntity>
}