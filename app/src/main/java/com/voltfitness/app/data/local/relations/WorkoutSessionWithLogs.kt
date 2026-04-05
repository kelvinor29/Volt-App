package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.WorkoutExerciseLogEntity
import com.voltfitness.app.data.local.entities.WorkoutSessionEntity

/**
 * Represents a completed or ongoing workout session with all its recorded exercise logs.
 */
data class WorkoutSessionWithLogs(
    @Embedded val session: WorkoutSessionEntity,
    @Relation(
        parentColumn = "sessionId",
        entityColumn = "sessionId"
    )
    val logs: List<WorkoutExerciseLogEntity>
)