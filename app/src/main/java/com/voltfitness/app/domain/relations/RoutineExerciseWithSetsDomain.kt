package com.voltfitness.app.domain.relations

import com.voltfitness.app.domain.model.RoutineExercise
import com.voltfitness.app.domain.model.RoutineExerciseSet

data class RoutineExerciseWithSetsDomain(
    val routineExercise: RoutineExercise,
    val sets: List<RoutineExerciseSet>
)