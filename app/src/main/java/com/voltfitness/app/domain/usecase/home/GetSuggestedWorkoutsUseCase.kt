package com.voltfitness.app.domain.usecase.home

import com.voltfitness.app.domain.relations.FolderWithRoutinesDomain
import com.voltfitness.app.ui.components.cards.ExerciseSummary
import javax.inject.Inject

class GetSuggestedWorkoutsUseCase @Inject constructor() {

    operator fun invoke(
        folders: List<FolderWithRoutinesDomain>
    ): List<ExerciseSummary> {

        val suggestions = mutableListOf<ExerciseSummary>()
        folders.forEach { folder ->
            folder.routines.forEach { routine ->
                if (suggestions.size < 3) {
                    suggestions += ExerciseSummary(
                        id = routine.id,
                        name = routine.name,
                        description = "${routine.daysPerWeek ?: 0} Days a Week - ${routine.goal ?: "Routine"}",
                        intensity = "123" // luego puedes calcular o dejar vacío si no está modelado
                    )
                }
            }
        }
        return suggestions
    }
}