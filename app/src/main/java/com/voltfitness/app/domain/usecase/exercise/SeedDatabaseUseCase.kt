package com.voltfitness.app.domain.usecase.exercise

import android.content.Context
import android.util.Log
import com.voltfitness.app.data.local.dao.ExerciseDao
import com.voltfitness.app.data.local.entities.ExerciseCatalogEntity
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.data.remote.dto.RemoteExerciseDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Seeds the Room database with exercises from the bundled assets/exercises.json.
 *
 * Idempotent: if exercises already exist, the operation is skipped.
 * Intended to run once on first app launch from the HomeViewModel.
 *
 * Also extracts catalog data (body parts, targets, equipment) directly
 * from the exercise list to avoid any additional API calls.
 */
class SeedDatabaseUseCase @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val exerciseDao: ExerciseDao
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend operator fun invoke() {
        try {
            if (exerciseDao.getCount() > 0) return

            val jsonString = context.assets
                .open("exercises.json")
                .bufferedReader()
                .use { it.readText() }

            val dtos = json.decodeFromString<List<RemoteExerciseDto>>(jsonString)

            // Insert in batches of 100 to avoid TransactionTooLargeException
            dtos.chunked(100).forEach { batch ->
                exerciseDao.insertExercises(batch.map { it.toEntity() })
            }

            seedCatalogs(dtos)
        } catch (e: Exception) {
            Log.e("SeedDatabase", "Error seeding database: ${e}")
        }
    }

    /**
     * Extracts unique body parts, targets, and equipment from the exercise
     * data and stores them as catalog entries for the filter UI.
     */
    private suspend fun seedCatalogs(dtos: List<RemoteExerciseDto>) {
        val catalogs = mutableListOf<ExerciseCatalogEntity>()

        dtos.map { it.bodyPart }.distinct().sorted().forEach {
            catalogs.add(ExerciseCatalogEntity(type = "body_part", value = it))
        }
        dtos.map { it.target }.distinct().sorted().forEach {
            catalogs.add(ExerciseCatalogEntity(type = "target", value = it))
        }
        dtos.map { it.equipment }.distinct().sorted().forEach {
            catalogs.add(ExerciseCatalogEntity(type = "equipment", value = it))
        }

        exerciseDao.insertCatalogs(catalogs)
    }
}
