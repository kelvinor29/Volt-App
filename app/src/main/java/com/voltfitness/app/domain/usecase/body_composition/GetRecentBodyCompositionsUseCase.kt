package com.voltfitness.app.domain.usecase.body_composition

import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentBodyCompositionsUseCase @Inject constructor(
    private val repository: BodyCompositionRepository
) {

    operator fun invoke(
        userId: Long,
        limit: Int
    ): Flow<List<BodyCompositionEntry>> =
        repository.getRecentEntries(userId, limit)
}