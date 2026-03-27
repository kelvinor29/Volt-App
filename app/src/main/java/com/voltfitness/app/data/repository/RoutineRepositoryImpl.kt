package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.RoutineDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.relations.RoutineDayFullDomain
import com.voltfitness.app.domain.relations.RoutineExerciseWithSetsDomain
import com.voltfitness.app.domain.relations.RoutineWithDaysDomain
import com.voltfitness.app.domain.repository.RoutineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val routineDao: RoutineDao
) : RoutineRepository {

    // ==================== ROUTINES ====================

    override fun getRoutineWithDays(routineId: Long): Flow<RoutineWithDaysDomain?> =
        routineDao.getRoutineWithDaysFlow(routineId).map { relation ->
            relation?.let {
                RoutineWithDaysDomain(
                    routine = it.routine.toDomain(),
                    days = it.days.map { day -> day.toDomain() }
                )
            }
        }

    override suspend fun getRoutineById(routineId: Long): Routine? =
        routineDao.getRoutineById(routineId)?.toDomain()

    override suspend fun upsertRoutine(routine: Routine): Long =
        routineDao.upsertRoutine(routine.toEntity())

    override suspend fun deleteRoutine(routineId: Long) =
        routineDao.deleteRoutineById(routineId)

    // ==================== ROUTINE DAYS ====================

    override fun getRoutineDayFull(dayId: Long): Flow<RoutineDayFullDomain?> =
        routineDao.getRoutineDayFullFlow(dayId).map { relation ->
            relation?.let {
                RoutineDayFullDomain(
                    day = it.day.toDomain(),
                    exercises = it.exercisesWithSets.map { exerciseWithSets ->
                        RoutineExerciseWithSetsDomain(
                            routineExercise = exerciseWithSets.exercise.toDomain(),
                            sets = exerciseWithSets.sets.map { set -> set.toDomain() }
                        )
                    }
                )
            }
        }

    override suspend fun upsertRoutineDay(day: RoutineDay): Long =
        routineDao.upsertRoutineDay(day.toEntity())

    override suspend fun upsertRoutineDays(days: List<RoutineDay>): List<Long> =
        routineDao.upsertRoutineDays(days.map { it.toEntity() })

    override suspend fun deleteRoutineDaysByRoutineId(routineId: Long) =
        routineDao.deleteRoutineDaysByRoutineId(routineId)
}
