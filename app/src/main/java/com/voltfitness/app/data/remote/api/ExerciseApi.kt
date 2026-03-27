package com.voltfitness.app.data.remote.api

import com.voltfitness.app.data.remote.dto.RemoteExerciseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for the ExerciseDB API on RapidAPI.
 * Authentication is handled via OkHttp interceptor in [NetworkModule].
 *
 * @see <a href="https://edb-docs.up.railway.app">ExerciseDB Docs</a>
 */
interface ExerciseApi {

    /** Fetch all exercises. Set limit=0 for the full list (requires PRO plan). */
    @GET("exercises")
    suspend fun getExercises(
        @Query("limit") limit: Int = 0,
        @Query("offset") offset: Int = 0
    ): List<RemoteExerciseDto>

    /** Search exercises by name (partial match, server-side). */
    @GET("exercises/name/{name}")
    suspend fun searchByName(
        @Path("name") name: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>

    // ==================== CATALOG ENDPOINTS ====================

    /** Returns all available body parts (e.g., "chest", "back", "legs"). */
    @GET("exercises/bodyPartList")
    suspend fun getBodyPartList(): List<String>

    /** Returns all available target muscles (e.g., "biceps", "quads"). */
    @GET("exercises/targetList")
    suspend fun getTargetList(): List<String>

    /** Returns all available equipment types (e.g., "dumbbell", "barbell"). */
    @GET("exercises/equipmentList")
    suspend fun getEquipmentList(): List<String>

    /** Fetch exercises filtered by body part. */
    @GET("exercises/bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(
        @Path("bodyPart") bodyPart: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>

    /** Fetch exercises filtered by target muscle. */
    @GET("exercises/target/{target}")
    suspend fun getExercisesByTarget(
        @Path("target") target: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>

    /** Fetch exercises filtered by equipment. */
    @GET("exercises/equipment/{equipment}")
    suspend fun getExercisesByEquipment(
        @Path("equipment") equipment: String,
        @Query("limit") limit: Int = 0
    ): List<RemoteExerciseDto>
}
