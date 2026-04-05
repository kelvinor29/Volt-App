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

/**
 * Hilt module for binding Repository interfaces to their data-layer implementations.
 *
 * This module ensures that the Domain layer remains decoupled from specific
 * data sources by providing the required [Singleton] instances through
 * the dependency graph.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the [UserRepository] interface to [UserRepositoryImpl].
     */
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    /**
     * Binds the [ExerciseRepository] interface to [ExerciseRepositoryImpl].
     */
    @Binds
    @Singleton
    abstract fun bindExerciseRepository(
        impl: ExerciseRepositoryImpl
    ): ExerciseRepository

    /**
     * Binds the [RoutineRepository] interface to [RoutineRepositoryImpl].
     */
    @Binds
    @Singleton
    abstract fun bindRoutineRepository(
        impl: RoutineRepositoryImpl
    ): RoutineRepository

    /**
     * Binds the [BodyCompositionRepository] interface to [BodyCompositionRepositoryImpl].
     */
    @Binds
    @Singleton
    abstract fun bindBodyCompositionRepository(
        impl: BodyCompositionRepositoryImpl
    ): BodyCompositionRepository

    /**
     * Binds the [FolderRepository] interface to [FolderRepositoryImpl].
     */
    @Binds
    @Singleton
    abstract fun bindFolderRepository(
        impl: FolderRepositoryImpl
    ): FolderRepository
}