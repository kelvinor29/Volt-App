package com.voltfitness.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.voltfitness.app.data.local.dao.BodyCompositionDao
import com.voltfitness.app.data.local.dao.ExerciseDao
import com.voltfitness.app.data.local.dao.FolderDao
import com.voltfitness.app.data.local.dao.RoutineDao
import com.voltfitness.app.data.local.dao.UserDao
import com.voltfitness.app.data.local.dao.WorkoutDao
import com.voltfitness.app.data.local.entities.BodyCompositionEntity
import com.voltfitness.app.data.local.entities.ExerciseCatalogEntity
import com.voltfitness.app.data.local.entities.ExerciseEntity
import com.voltfitness.app.data.local.entities.FolderEntity
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseSetEntity
import com.voltfitness.app.data.local.entities.UserEntity
import com.voltfitness.app.data.local.entities.WorkoutExerciseLogEntity
import com.voltfitness.app.data.local.entities.WorkoutSessionEntity

/**
 * Main Room database configuration for the Volt Fitness application.
 *
 * This database acts as the single source of truth for the offline-first architecture,
 * managing user profiles, training routines, exercise catalogs, and performance logs.
 *
 * @see Converters for handling complex data types like JSON lists.
 * @see DatabaseModule for Hilt dependency injection configuration.
 */
@Database(
    entities = [
        BodyCompositionEntity::class,
        ExerciseEntity::class,
        FolderEntity::class,
        RoutineDayEntity::class,
        RoutineEntity::class,
        RoutineExerciseEntity::class,
        RoutineExerciseSetEntity::class,
        UserEntity::class,
        WorkoutExerciseLogEntity::class,
        WorkoutSessionEntity::class,
        ExerciseCatalogEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Data Access Object for user profile management.
     */
    abstract fun userDao(): UserDao

    /**
     * Data Access Object for organizing routines into folders.
     */
    abstract fun folderDao(): FolderDao

    /**
     * Data Access Object for training routine structures and days.
     */
    abstract fun routineDao(): RoutineDao

    /**
     * Data Access Object for exercise definitions and catalog filters.
     */
    abstract fun exerciseDao(): ExerciseDao

    /**
     * Data Access Object for live workout tracking and historical logs.
     */
    abstract fun workoutDao(): WorkoutDao

    /**
     * Data Access Object for body metrics and composition tracking.
     */
    abstract fun bodyCompositionDao(): BodyCompositionDao
}