package com.voltfitness.app.domain.relations

import com.voltfitness.app.domain.model.RoutineDay

/**
 * A training day with its full exercise list (each exercise includes its sets).
 *
 * @param day    The [RoutineDay] metadata (id, routineId, order, name).
 * @param exercises Each exercise assigned to this day, wrapped with its sets.
 */
data class RoutineDayFullDomain(
    val day: RoutineDay,
    val exercises: List<RoutineExerciseWithSetsDomain>
)