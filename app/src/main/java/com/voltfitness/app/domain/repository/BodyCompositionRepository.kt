package com.voltfitness.app.domain.repository

import com.voltfitness.app.domain.model.BodyCompositionEntry
import kotlinx.coroutines.flow.Flow

interface BodyCompositionRepository {

    fun getRecentEntries(
        userId: Long,
        limit: Int
    ): Flow<List<BodyCompositionEntry>>

    fun getLatestEntry(
        userId: Long
    ): Flow<BodyCompositionEntry?>

    suspend fun insertEntry(entry: BodyCompositionEntry)
}