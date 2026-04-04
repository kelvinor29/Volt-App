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
import com.voltfitness.app.data.repository.BodyCompositionRepositoryImpl
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "volt.db"
        ).fallbackToDestructiveMigration()
            .build()
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