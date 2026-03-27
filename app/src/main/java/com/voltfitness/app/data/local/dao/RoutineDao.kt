//package com.voltfitness.app.data.local.dao
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Transaction
//import com.voltfitness.app.data.local.entities.RoutineDayEntity
//import com.voltfitness.app.data.local.entities.RoutineEntity
//import com.voltfitness.app.data.local.relations.RoutineDayFull
//import com.voltfitness.app.data.local.relations.RoutineWithDays
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface RoutineDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertRoutine(routine: RoutineEntity): Long
//
//    @Query("SELECT * FROM routines WHERE routineId = :id")
//    suspend fun getRoutineById(id: Long): RoutineEntity?
//
//    @Transaction
//    @Query("SELECT * FROM routines WHERE routineId = :id")
//    fun getRoutineWithDaysFlow(id: Long): Flow<RoutineWithDays?>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertRoutineDay(day: RoutineDayEntity): Long
//
//    @Query("SELECT * FROM routine_days WHERE dayId = :id")
//    suspend fun getRoutineDayById(id: Long): RoutineDayEntity?
//
//    @Transaction
//    @Query("SELECT * FROM routine_days WHERE dayId = :id")
//    fun getRoutineDayFullFlow(id: Long): Flow<RoutineDayFull?>
//
//    @Delete
//    suspend fun deleteRoutine(routine: RoutineEntity)
//
//    @Delete
//    suspend fun deleteRoutineDay(day: RoutineDayEntity)
//}
//

package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineEntity
import com.voltfitness.app.data.local.relations.RoutineDayFull
import com.voltfitness.app.data.local.relations.RoutineWithDays
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    // ==================== ROUTINES ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutine(routine: RoutineEntity): Long

    @Query("SELECT * FROM routines WHERE routineId = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity?

    @Transaction
    @Query("SELECT * FROM routines WHERE routineId = :id")
    fun getRoutineWithDaysFlow(id: Long): Flow<RoutineWithDays?>

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    @Query("DELETE FROM routines WHERE routineId = :routineId")
    suspend fun deleteRoutineById(routineId: Long)

    // ==================== ROUTINE DAYS ====================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutineDay(day: RoutineDayEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoutineDays(days: List<RoutineDayEntity>): List<Long>

    @Query("SELECT * FROM routine_days WHERE dayId = :id")
    suspend fun getRoutineDayById(id: Long): RoutineDayEntity?

    @Transaction
    @Query("SELECT * FROM routine_days WHERE dayId = :id")
    fun getRoutineDayFullFlow(id: Long): Flow<RoutineDayFull?>

    @Delete
    suspend fun deleteRoutineDay(day: RoutineDayEntity)

    @Query("DELETE FROM routine_days WHERE routineId = :routineId")
    suspend fun deleteRoutineDaysByRoutineId(routineId: Long)
}
