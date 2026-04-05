package com.voltfitness.app.data.remote.source

import com.voltfitness.app.data.remote.api.ExerciseApi
import com.voltfitness.app.data.remote.dto.RemoteExerciseDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source that encapsulates interactions with the ExerciseDB API.
 *
 * Acts as a thin wrapper around [ExerciseApi], providing a clean interface for
 * the repository layer while decoupling API-specific concerns from business logic.
 *
 * @property api The Retrofit-generated service for network operations.
 */
@Singleton
class ExerciseRemoteDataSource @Inject constructor(
    private val api: ExerciseApi
) {
    /**
     * Retrieves a list of exercises from the remote server.
     *
     * @param limit Maximum number of exercises to return. Defaults to 0 (all).
     * @param offset Starting position in the collection for pagination.
     * @return List of [RemoteExerciseDto] received from the API.
     */
    suspend fun getExercises(limit: Int = 0, offset: Int = 0): List<RemoteExerciseDto> =
        api.getExercises(limit = limit, offset = offset)

    /**
     * Fetches the complete list of available body part categories.
     */
    suspend fun getBodyPartList(): List<String> = api.getBodyPartList()

    /**
     * Fetches the complete list of available target muscle groups.
     */
    suspend fun getTargetList(): List<String> = api.getTargetList()

    /**
     * Fetches the complete list of available gym equipment categories.
     */
    suspend fun getEquipmentList(): List<String> = api.getEquipmentList()
}