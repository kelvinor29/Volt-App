package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voltfitness.app.data.local.entities.ExerciseCatalogEntity
import com.voltfitness.app.data.local.entities.ExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    // --- Core Exercises ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("SELECT * FROM exercises ORDER BY name")
    fun getAllExercisesFlow(): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises ORDER BY name")
    suspend fun getAllExercises(): List<ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE exerciseId = :id")
    suspend fun getExerciseById(id: String): ExerciseEntity?

    @Query("SELECT * FROM exercises WHERE exerciseId IN (:ids)")
    suspend fun getExercisesByIds(ids: List<String>): List<ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE bodyPart = :bodyPart ORDER BY name")
    fun getExercisesByBodyPartFlow(bodyPart: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE target = :target ORDER BY name")
    fun getExercisesByTargetFlow(target: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE equipment = :equipment ORDER BY name")
    fun getExercisesByEquipmentFlow(equipment: String): Flow<List<ExerciseEntity>>

    @Query("SELECT * FROM exercises WHERE name LIKE '%' || :query || '%' ORDER BY name")
    fun searchExercisesFlow(query: String): Flow<List<ExerciseEntity>>

    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getCount(): Int

    // --- Catalog Management (Filters) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalogs(catalogs: List<ExerciseCatalogEntity>)

    @Query("DELETE FROM exercise_catalogs WHERE type = :type")
    suspend fun deleteCatalogsByType(type: String)

    @Query("SELECT value FROM exercise_catalogs WHERE type = :type ORDER BY value")
    suspend fun getCatalogValues(type: String): List<String>

    @Query("SELECT value FROM exercise_catalogs WHERE type = :type ORDER BY value")
    fun getCatalogValuesFlow(type: String): Flow<List<String>>
}