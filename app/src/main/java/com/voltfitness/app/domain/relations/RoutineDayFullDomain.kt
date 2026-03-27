package com.voltfitness.app.domain.relations

import com.voltfitness.app.domain.model.RoutineDay

data class RoutineDayFullDomain(
    val day: RoutineDay,
    val exercises: List<RoutineExerciseWithSetsDomain>
)