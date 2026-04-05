package com.voltfitness.app.data.remote.api

import com.voltfitness.app.data.remote.dto.RemoteExerciseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface defining the endpoints for the ExerciseDB API via RapidAPI.
 * Authentication and base URL configuration are managed by the OkHttp interceptor.
 *
 * @see <a href="https://edb-docs.up.railway.app">ExerciseDB Documentation</a>
 */
interface ExerciseApi {

    /**
     * Retrieves a list of all available exercises.
     *
     * @param limit Maximum number of results to return. Use 0 for full list (if supported by API plan).
     * @param offset Number of results to skip for pagination.
     */
    @GET("exercises")
    suspend fun getExercises(
        @Query("limit") limit: Int = 0,
        @Query("offset") offset: Int = 0
    ): List<RemoteExerciseDto>

    /**
     * Performs a server-side search for exercises by name using partial matching.
     *
     * @param name The exercise name or keyword to search for.
     * @param limit Maximum number of results to return.
     */
    @GET("exercises/name/{name}")
    suspend fun searchByName(
        @Path("name") name: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>

    // ==================== CATALOG ENDPOINTS ====================

    /**
     * Fetches all unique body part categories available in the database.
     * Examples: "chest", "back", "cardio".
     */
    @GET("exercises/bodyPartList")
    suspend fun getBodyPartList(): List<String>

    /**
     * Fetches all unique target muscle groups.
     * Examples: "abs", "biceps", "glutes".
     */
    @GET("exercises/targetList")
    suspend fun getTargetList(): List<String>

    /**
     * Fetches all available equipment types.
     * Examples: "dumbbell", "kettlebell", "body weight".
     */
    @GET("exercises/equipmentList")
    suspend fun getEquipmentList(): List<String>

    /**
     * Filters exercises based on a specific body part.
     *
     * @param bodyPart The exact category name retrieved from [getBodyPartList].
     * @param limit Maximum number of results to return.
     */
    @GET("exercises/bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(
        @Path("bodyPart") bodyPart: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>

    /**
     * Filters exercises based on a specific target muscle.
     *
     * @param target The exact muscle name retrieved from [getTargetList].
     * @param limit Maximum number of results to return.
     */
    @GET("exercises/target/{target}")
    suspend fun getExercisesByTarget(
        @Path("target") target: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>

    /**
     * Filters exercises based on a specific equipment type.
     *
     * @param equipment The exact equipment name retrieved from [getEquipmentList].
     * @param limit Maximum number of results to return.
     */
    @GET("exercises/equipment/{equipment}")
    suspend fun getExercisesByEquipment(
        @Path("equipment") equipment: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>
}