package com.voltfitness.app.domain.relations

import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay

data class RoutineWithDaysDomain(
    val routine: Routine,
    val days: List<RoutineDay>
)