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

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val remoteDataSource: ExerciseRemoteDataSource
) : ExerciseRepository {

    // ==================== EXERCISES ====================

    override fun getAllExercises(): Flow<List<Exercise>> =
        exerciseDao.getAllExercisesFlow().map { list -> list.map { it.toDomain() } }

    override suspend fun getAllExercisesList(): List<Exercise> =
        exerciseDao.getAllExercises().map { it.toDomain() }

    override fun searchExercises(query: String): Flow<List<Exercise>> =
        exerciseDao.searchExercisesFlow(query).map { list -> list.map { it.toDomain() } }

    override fun getExercisesByBodyPart(bodyPart: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByBodyPartFlow(bodyPart).map { list -> list.map { it.toDomain() } }

    override fun getExercisesByTarget(target: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByTargetFlow(target).map { list -> list.map { it.toDomain() } }

    override fun getExercisesByEquipment(equipment: String): Flow<List<Exercise>> =
        exerciseDao.getExercisesByEquipmentFlow(equipment).map { list -> list.map { it.toDomain() } }

    override suspend fun getExerciseById(id: String): Exercise? =
        exerciseDao.getExerciseById(id)?.toDomain()

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
