package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.BodyCompositionDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BodyCompositionRepositoryImpl @Inject constructor(
    private val dao: BodyCompositionDao
) : BodyCompositionRepository {

    override fun getRecentEntries(
        userId: Long,
        limit: Int
    ): Flow<List<BodyCompositionEntry>> =
        dao.getRecentEntries(userId, limit).map { list ->
            list.map { it.toDomain() }
        }

    override fun getLatestEntry(
        userId: Long
    ): Flow<BodyCompositionEntry?> =
        dao.getLatestEntry(userId).map { entity ->
            entity?.toDomain()
        }

    override fun hasEntries(userId: Long): Flow<Boolean> {
        return dao.hasEntries(userId)
    }

    override suspend fun insertEntry(entry: BodyCompositionEntry) {
        dao.insertEntry(entry.toEntity())
    }
}