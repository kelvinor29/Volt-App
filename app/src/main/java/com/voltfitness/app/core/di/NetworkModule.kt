package com.voltfitness.app.core.di

import com.voltfitness.app.BuildConfig
import com.voltfitness.app.data.remote.api.ExerciseApi
import com.voltfitness.app.data.remote.source.ExerciseRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://exercisedb.p.rapidapi.com/"
    private const val RAPID_API_HOST = "exercisedb.p.rapidapi.com"

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

        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)

        // Only log in debug builds
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return builder.build()
    }


    @Provides
    @Singleton
    fun provideExerciseApi(okHttpClient: OkHttpClient): ExerciseApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExerciseRemoteDataSource(api: ExerciseApi): ExerciseRemoteDataSource {
        return ExerciseRemoteDataSource(api)
    }
}
