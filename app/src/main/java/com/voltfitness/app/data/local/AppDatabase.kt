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
        ExerciseCatalogEntity::class,
    ],
    version = 1,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun folderDao(): FolderDao
    abstract fun routineDao(): RoutineDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun bodyCompositionDao(): BodyCompositionDao
}