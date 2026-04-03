package com.voltfitness.app.domain.repository

import com.voltfitness.app.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for exercise data.
 * Follows offline-first pattern: local Room DB is the single source of truth,
 * remote API is used only for initial population and periodic refresh.
 */
interface ExerciseRepository {

    /** Observes all exercises from local cache. */
    fun getAllExercises(): Flow<List<Exercise>>

    /** One-shot load for in-memory fuzzy search. */
    suspend fun getAllExercisesList(): List<Exercise>

    /** Basic SQL LIKE search (fallback). */
    fun searchExercises(query: String): Flow<List<Exercise>>

    /** Observes exercises filtered by body part. */
    fun getExercisesByBodyPart(bodyPart: String): Flow<List<Exercise>>

    /** Observes exercises filtered by target muscle. */
    fun getExercisesByTarget(target: String): Flow<List<Exercise>>

    /** Observes exercises filtered by equipment. */
    fun getExercisesByEquipment(equipment: String): Flow<List<Exercise>>

    /** Gets a single exercise by ID. */
    suspend fun getExerciseById(id: String): Exercise?

    /** Gets a list of exercises by ID. */
    suspend fun getExercisesByIds(ids: List<String>): List<Exercise>

    /** Fetches exercises from API and caches them locally. */
    suspend fun refreshExercisesFromRemote()

    /** Checks if local cache has data. */
    suspend fun isCacheEmpty(): Boolean

    // ==================== CATALOGS ====================

    /** Gets all body parts from local catalog. */
    suspend fun getBodyParts(): List<String>

    /** Gets all target muscles from local catalog. */
    suspend fun getTargets(): List<String>

    /** Gets all equipment types from local catalog. */
    suspend fun getEquipment(): List<String>

    /** Fetches catalog lists from API and caches them locally. */
    suspend fun refreshCatalogsFromRemote()
}
