package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.voltfitness.app.data.local.entities.WorkoutExerciseLogEntity
import com.voltfitness.app.data.local.entities.WorkoutSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Update
    suspend fun updateSession(session: WorkoutSessionEntity)

    @Query("SELECT * FROM workout_sessions WHERE userId = :userId ORDER BY startedAt DESC")
    fun getSessionsFlow(userId: Long): Flow<List<WorkoutSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseLogs(logs: List<WorkoutExerciseLogEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseLog(log: WorkoutExerciseLogEntity): Long

    @Update
    suspend fun updateExerciseLog(log: WorkoutExerciseLogEntity)

    @Query("SELECT * FROM workout_exercise_logs WHERE sessionId = :sessionId ORDER BY setOrder ASC")
    fun getLogsForSessionFlow(sessionId: Long): Flow<List<WorkoutExerciseLogEntity>>

    @Query(
        """
        SELECT * FROM workout_exercise_logs
        WHERE userId = :userId AND exerciseId = :exerciseId
        ORDER BY logId DESC
        LIMIT 1
    """
    )
    suspend fun getLastLogForExercise(userId: Long, exerciseId: String): WorkoutExerciseLogEntity?
}