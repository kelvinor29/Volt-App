package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voltfitness.app.data.local.entities.BodyCompositionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BodyCompositionDao {

    @Query(
        """
        SELECT * FROM body_composition_entries
        WHERE userId = :userId
        ORDER BY dateEpochDay DESC
        LIMIT :limit
        """
    )
    fun getRecentEntries(
        userId: Long,
        limit: Int
    ): Flow<List<BodyCompositionEntity>>

    @Query(
        """
        SELECT * FROM body_composition_entries
        WHERE userId = :userId
        ORDER BY dateEpochDay DESC
        LIMIT 1
        """
    )
    fun getLatestEntry(
        userId: Long
    ): Flow<BodyCompositionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: BodyCompositionEntity)
}
