package com.voltfitness.app.data.local.dao

import androidx.room.*
import com.voltfitness.app.data.local.entities.FolderEntity
import com.voltfitness.app.data.local.relations.FolderWithRoutines
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Upsert
    suspend fun upsertFolder(folder: FolderEntity): Long

    @Query("SELECT * FROM folders WHERE folderId = :id")
    suspend fun getById(id: Long): FolderEntity?

    @Query("SELECT * FROM folders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getFoldersFlow(userId: Long): Flow<List<FolderEntity>>

    /**
     * Retrieves all folders and their nested routines in a single transaction.
     */
    @Transaction
    @Query("SELECT * FROM folders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getFoldersWithRoutinesFlow(userId: Long): Flow<List<FolderWithRoutines>>

    @Query("SELECT EXISTS(SELECT 1 FROM folders WHERE userId = :userId AND name = :name LIMIT 1)")
    suspend fun existsByName(userId: Long, name: String): Boolean

    @Delete
    suspend fun delete(folder: FolderEntity)
}