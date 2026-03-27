package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.WorkoutExerciseLogEntity
import com.voltfitness.app.data.local.entities.WorkoutSessionEntity

data class WorkoutSessionWithLogs(
    @Embedded val session: WorkoutSessionEntity,
    @Relation(
        parentColumn = "sessionId",
        entityColumn = "sessionId"
    )
    val logs: List<WorkoutExerciseLogEntity>
)