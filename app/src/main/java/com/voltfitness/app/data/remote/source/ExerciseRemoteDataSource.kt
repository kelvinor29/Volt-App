package com.voltfitness.app.data.remote.source

import com.voltfitness.app.data.remote.api.ExerciseApi
import com.voltfitness.app.data.remote.dto.RemoteExerciseDto
import javax.inject.Inject

/**
 * Remote data source wrapping [ExerciseApi] calls.
 * Centralizes error handling and provides a clean interface
 * for the repository layer.
 */
class ExerciseRemoteDataSource @Inject constructor(
    private val api: ExerciseApi
) {
    /** Fetches all exercises (limit=0 returns full list on PRO plan). */
    suspend fun getExercises(limit: Int = 0, offset: Int = 0): List<RemoteExerciseDto> =
        api.getExercises(limit = limit, offset = offset)

    suspend fun getBodyPartList(): List<String> = api.getBodyPartList()

    suspend fun getTargetList(): List<String> = api.getTargetList()

    suspend fun getEquipmentList(): List<String> = api.getEquipmentList()
}
