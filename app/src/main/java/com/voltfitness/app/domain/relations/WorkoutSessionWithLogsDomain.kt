package com.voltfitness.app.domain.relations

import com.voltfitness.app.domain.model.WorkoutExerciseLog
import com.voltfitness.app.domain.model.WorkoutSession

data class WorkoutSessionWithLogsDomain(
    val session: WorkoutSession,
    val logs: List<WorkoutExerciseLog>
)