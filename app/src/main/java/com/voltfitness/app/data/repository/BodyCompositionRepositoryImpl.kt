package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.BodyCompositionDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [BodyCompositionRepository] that coordinates data operations
 * for user body metrics between the local database and the domain layer.
 */
class BodyCompositionRepositoryImpl @Inject constructor(
    private val dao: BodyCompositionDao
) : BodyCompositionRepository {

    /**
     * Streams a list of historical body composition records.
     *
     * @param userId Unique identifier for the user.
     * @param limit Maximum number of records to retrieve, typically used for charts or summaries.
     * @return A [Flow] of domain-mapped composition entries.
     */
    override fun getRecentEntries(
        userId: Long,
        limit: Int
    ): Flow<List<BodyCompositionEntry>> =
        dao.getRecentEntries(userId, limit).map { list ->
            list.map { it.toDomain() }
        }

    /**
     * Observes the single most recent body composition entry.
     * Useful for displaying "current" stats on dashboards.
     */
    override fun getLatestEntry(
        userId: Long
    ): Flow<BodyCompositionEntry?> =
        dao.getLatestEntry(userId).map { entity ->
            entity?.toDomain()
        }

    /**
     * Checks for the existence of any records to determine if the user has
     * an established baseline.
     */
    override fun hasEntries(userId: Long): Flow<Boolean> {
        return dao.hasEntries(userId)
    }

    /**
     * Persists a new body composition measurement.
     * Maps the [BodyCompositionEntry] domain model back to a data [Entity].
     */
    override suspend fun insertEntry(entry: BodyCompositionEntry) {
        dao.insertEntry(entry.toEntity())
    }
}