package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.relations.RoutineDayWithExercises
import com.voltfitness.app.data.local.relations.RoutineWithFullDays
import com.voltfitness.app.domain.relations.RoutineDayFullDomain
import com.voltfitness.app.domain.relations.RoutineExerciseWithSetsDomain
import com.voltfitness.app.domain.relations.RoutineWithFullDaysDomain

/**
 * Maps the Room [RoutineWithFullDays] relation to the domain [RoutineWithFullDaysDomain].
 *
 * This is the single mapping path for the full routine hierarchy used by
 * [GetRoutineDetailUseCase] and [RoutineEditorViewModel].
 */
fun RoutineWithFullDays.toDomain(): RoutineWithFullDaysDomain =
    RoutineWithFullDaysDomain(
        routine = routine.toDomain(),
        days = days
            .sortedBy { it.day.dayOrder }
            .map { it.toDomain() }
    )

fun RoutineDayWithExercises.toDomain(): RoutineDayFullDomain =
    RoutineDayFullDomain(
        day = day.toDomain(),
        exercises = exercises
            .sortedBy { it.exercise.orderInDay }
            .map { exerciseWithSets ->
                RoutineExerciseWithSetsDomain(
                    routineExercise = exerciseWithSets.exercise.toDomain(),
                    sets = exerciseWithSets.sets
                        .sortedBy { it.setOrder }
                        .map { it.toDomain() }
                )
            }
    )