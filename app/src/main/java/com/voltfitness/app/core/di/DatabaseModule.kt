package com.voltfitness.app.core.di

import android.content.Context
import androidx.room.Room
import com.voltfitness.app.data.local.AppDatabase
import com.voltfitness.app.data.local.dao.BodyCompositionDao
import com.voltfitness.app.data.local.dao.ExerciseDao
import com.voltfitness.app.data.local.dao.FolderDao
import com.voltfitness.app.data.local.dao.RoutineDao
import com.voltfitness.app.data.local.dao.UserDao
import com.voltfitness.app.data.local.dao.WorkoutDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DATABASE_NAME = "volt.db"

/**
 * Hilt module for providing Room database and DAO dependencies.
 *
 * Configures the centralized [AppDatabase] and exposes its DAOs to the dependency graph.
 * All providers are scoped to the [SingletonComponent] to ensure a single database instance.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Initializes the Room database instance using the application context.
     * Fallback destructive migration is disabled to prevent accidental data loss in production.
     */
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideFolderDao(db: AppDatabase): FolderDao = db.folderDao()

    @Provides
    fun provideRoutineDao(db: AppDatabase): RoutineDao = db.routineDao()

    @Provides
    fun provideExerciseDao(db: AppDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    fun provideWorkoutDao(db: AppDatabase): WorkoutDao = db.workoutDao()

    @Provides
    fun provideBodyCompositionDao(db: AppDatabase): BodyCompositionDao = db.bodyCompositionDao()
}