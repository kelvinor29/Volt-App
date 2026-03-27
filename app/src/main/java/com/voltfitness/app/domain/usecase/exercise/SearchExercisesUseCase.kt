package com.voltfitness.app.domain.usecase.exercise

import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Provides search and filtering capabilities over the local exercise catalog.
 * Delegates to [ExerciseRepository] for data access.
 */
class SearchExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    fun all(): Flow<List<Exercise>> =
        exerciseRepository.getAllExercises()

    fun byQuery(query: String): Flow<List<Exercise>> =
        exerciseRepository.searchExercises(query)

    fun byBodyPart(bodyPart: String): Flow<List<Exercise>> =
        exerciseRepository.getExercisesByBodyPart(bodyPart)

    fun byTarget(target: String): Flow<List<Exercise>> =
        exerciseRepository.getExercisesByTarget(target)

    fun byEquipment(equipment: String): Flow<List<Exercise>> =
        exerciseRepository.getExercisesByEquipment(equipment)

    suspend fun allList(): List<Exercise> =
        exerciseRepository.getAllExercisesList()
}
