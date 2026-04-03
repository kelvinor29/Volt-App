package com.voltfitness.app.domain.relations

import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay

/**
 * Aggregate domain model that carries a full routine snapshot:
 * the routine metadata, each training day, and every exercise
 * (with its sets) assigned to each day.
 *
 * Used by [GetRoutineDetailUseCase] and [RoutineEditorViewModel]
 * to load all the data needed by the editor in a single reactive stream.
 *
 * Replaces [RoutineWithDaysDomain] for the editor flow; the simpler
 * model is kept for contexts that do not need exercise detail.
 */
data class RoutineWithFullDaysDomain(
    val routine: Routine,
    val days: List<RoutineDayFullDomain>
)