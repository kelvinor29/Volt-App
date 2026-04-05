package com.voltfitness.app.core.di

import com.voltfitness.app.BuildConfig
import com.voltfitness.app.data.remote.api.ExerciseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Hilt module for providing network-related dependencies.
 *
 * Configures the communication layer with RapidAPI using Kotlinx Serialization.
 * Optimized for resilience against external API schema changes.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://exercisedb.p.rapidapi.com/"
    private const val RAPID_API_HOST = "exercisedb.p.rapidapi.com"
    private const val CONTENT_TYPE = "application/json"

    /**
     * Provides a configured [Json] instance for robust deserialization.
     *
     * Configuration:
     * - [ignoreUnknownKeys]: Prevents crashes when the API adds new fields.
     * - [coerceInputValues]: Provides safety for null/missing non-nullable types.
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    /**
     * Configures [OkHttpClient] with authentication and logging.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val authInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("x-rapidapi-key", BuildConfig.EXERCISE_DB_API_KEY)
                .addHeader("x-rapidapi-host", RAPID_API_HOST)
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }

    /**
     * Provides the [ExerciseApi] service using Kotlinx Serialization converter.
     */
    @Provides
    @Singleton
    fun provideExerciseApi(
        okHttpClient: OkHttpClient,
        json: Json
    ): ExerciseApi {
        val contentType = CONTENT_TYPE.toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ExerciseApi::class.java)
    }
}
