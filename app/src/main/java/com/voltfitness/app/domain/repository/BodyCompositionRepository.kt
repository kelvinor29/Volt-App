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

    /**
     * Emits `true` if the user has at least one body composition entry.
     * Used to drive the first-launch redirect to [AddBodyCompositionScreen].
     */
    fun hasEntries(userId: Long): Flow<Boolean>

    suspend fun insertEntry(entry: BodyCompositionEntry)
}