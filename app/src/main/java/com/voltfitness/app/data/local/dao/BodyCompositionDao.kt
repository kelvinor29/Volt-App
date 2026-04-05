package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voltfitness.app.data.local.entities.BodyCompositionEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for managing body composition metrics.
 */
@Dao
interface BodyCompositionDao {

    /**
     * Observes a limited list of the most recent entries for a specific user.
     */
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

    /**
     * Checks if the user has any recorded body composition entries.
     */
    @Query("SELECT COUNT(*) > 0 FROM body_composition_entries WHERE userId = :userId")
    fun hasEntries(userId: Long): Flow<Boolean>

    /**
     * Observes the single most recent record.
     */
    @Query(
        """
        SELECT * FROM body_composition_entries
        WHERE userId = :userId
        ORDER BY dateEpochDay DESC
        LIMIT 1
        """
    )
    fun getLatestEntry(userId: Long): Flow<BodyCompositionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: BodyCompositionEntity)
}