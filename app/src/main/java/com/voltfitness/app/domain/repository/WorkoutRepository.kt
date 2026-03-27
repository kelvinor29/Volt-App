package com.voltfitness.app.domain.repository

import com.voltfitness.app.domain.model.WorkoutExerciseLog
import com.voltfitness.app.domain.model.WorkoutSession
import com.voltfitness.app.domain.relations.WorkoutSessionWithLogsDomain
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun getSessions(userId: Long): Flow<List<WorkoutSession>>
    fun getSessionWithLogs(sessionId: Long): Flow<WorkoutSessionWithLogsDomain?>

    suspend fun startSession(
        routineId: Long?,
        dayId: Long?,
        userId: Long
    ): WorkoutSession

    suspend fun saveLogs(logs: List<WorkoutExerciseLog>)
    suspend fun updateLog(log: WorkoutExerciseLog)
    suspend fun finishSession(session: WorkoutSession)

    suspend fun getLastLogForExercise(userId: Long, exerciseId: String): WorkoutExerciseLog?
}