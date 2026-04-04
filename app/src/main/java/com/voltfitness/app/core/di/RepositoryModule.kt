package com.voltfitness.app.core.di

import com.voltfitness.app.data.repository.BodyCompositionRepositoryImpl
import com.voltfitness.app.data.repository.ExerciseRepositoryImpl
import com.voltfitness.app.data.repository.FolderRepositoryImpl
import com.voltfitness.app.data.repository.RoutineRepositoryImpl
import com.voltfitness.app.data.repository.UserRepositoryImpl
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import com.voltfitness.app.domain.repository.ExerciseRepository
import com.voltfitness.app.domain.repository.FolderRepository
import com.voltfitness.app.domain.repository.RoutineRepository
import com.voltfitness.app.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(
        impl: ExerciseRepositoryImpl
    ): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindRoutineRepositoryImpl(
        impl: RoutineRepositoryImpl
    ): RoutineRepository

    @Binds
    @Singleton
    abstract fun bindBodyCompositionRepositoryImpl(
        impl: BodyCompositionRepositoryImpl
    ): BodyCompositionRepository


    @Binds
    @Singleton
    abstract fun bindFolderRepository(
        impl: FolderRepositoryImpl
    ): FolderRepository

//    @Binds
//    @Singleton
//    abstract fun bindRoutineRepository(
//        impl: RoutineRepositoryImpl
//    ): RoutineRepository
}
