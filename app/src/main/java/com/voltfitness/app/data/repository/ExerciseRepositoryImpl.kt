package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.ExerciseDao
import com.voltfitness.app.data.local.entities.ExerciseCatalogEntity
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.data.remote.source.ExerciseRemoteDataSource
import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [ExerciseRepository] providing an offline-first data strategy.
 * It uses [ExerciseDao] as the single source of truth and [ExerciseRemoteDataSource]
 * for data synchronization.
 */
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val remoteDataSource: ExerciseRemoteDataSource
) : ExerciseRepository {

    // ==================== EXERCISES ====================

    /**
     * Observes all cached exercises from the local database.
     */
    override fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAllExercisesFlow().map { list -> list.map { it.toDomain() } }

    /**
     * Retrieves a one-shot list of all cached exercises.
     */
    override suspend fun getAllExercisesList(): List<Exercise> =
        exerciseDao.getAllExercises().map { it.toDomain() }

    /**
     * Searches for exercises in the local cache by name or muscle group.
     */
    override fun searchExercises(query: String): Flow<List<Exercise>> =
        exerciseDao.searchExercisesFlow(query).map { list -> list.map { it.toDomain() } }

    /**
     * Filters cached exercises by body part category (e.g., "cardio", "waist").
     */
    override fun getExercisesByBodyPart(bodyPart: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByBodyPartFlow(bodyPart).map { list -> list.map { it.toDomain() } }

    /**
     * Filters cached exercises by specific target muscle (e.g., "abs", "quads").
     */
    override fun getExercisesByTarget(target: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByTargetFlow(target).map { list -> list.map { it.toDomain() } }

    /**
     * Filters cached exercises by the type of equipment required.
     */
    override fun getExercisesByEquipment(equipment: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByEquipmentFlow(equipment).map { list -> list.map { it.toDomain() } }

    override suspend fun getExerciseById(id: String): Exercise? =
        exerciseDao.getExerciseById(id)?.toDomain()

    override suspend fun getExercisesByIds(ids: List<String>): List<Exercise> =
        exerciseDao.getExercisesByIds(ids)?.map { it.toDomain() } ?: emptyList()

    /**
     * Synchronizes the local database with the remote API.
     * Silently handles exceptions to ensure offline functionality is not interrupted.
     */
    override suspend fun refreshExercisesFromRemote() {
        try {
            val remote = remoteDataSource.getExercises()
            exerciseDao.insertExercises(remote.map { it.toEntity() })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun isCacheEmpty(): Boolean =
        exerciseDao.getCount() == 0

    // ==================== CATALOGS ====================

    override suspend fun getBodyParts(): List<String> =
        exerciseDao.getCatalogValues(CATALOG_BODY_PART)

    override suspend fun getTargets(): List<String> =
        exerciseDao.getCatalogValues(CATALOG_TARGET)

    override suspend fun getEquipment(): List<String> =
        exerciseDao.getCatalogValues(CATALOG_EQUIPMENT)

    /**
     * Fetches all metadata lists from the remote source and updates the local catalog tables.
     */
    override suspend fun refreshCatalogsFromRemote() {
        try {
            val bodyParts = remoteDataSource.getBodyPartList()
            val targets = remoteDataSource.getTargetList()
            val equipment = remoteDataSource.getEquipmentList()

            saveCatalog(CATALOG_BODY_PART, bodyParts)
            saveCatalog(CATALOG_TARGET, targets)
            saveCatalog(CATALOG_EQUIPMENT, equipment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Replaces the local catalog for a specific type with new data.
     */
    private suspend fun saveCatalog(type: String, values: List<String>) {
        exerciseDao.deleteCatalogsByType(type)
        exerciseDao.insertCatalogs(
            values.map { ExerciseCatalogEntity(type = type, value = it) }
        )
    }

    companion object {
        const val CATALOG_BODY_PART = "body_part"
        const val CATALOG_TARGET = "target"
        const val CATALOG_EQUIPMENT = "equipment"
    }
}