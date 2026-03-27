//package com.voltfitness.app.data.local.dao
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Transaction
//import com.voltfitness.app.data.local.entities.FolderEntity
//import com.voltfitness.app.data.local.relations.FolderWithRoutines
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface FolderDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun upsertFolder(folder: FolderEntity): Long
//
//    @Query("SELECT * FROM folders WHERE userId = :userId ORDER BY createdAt DESC")
//    fun getFoldersFlow(userId: Long): Flow<List<FolderEntity>>
//
//    @Transaction
//    @Query("SELECT * FROM folders WHERE userId = :userId ORDER BY createdAt DESC")
//    fun getFoldersWithRoutinesFlow(userId: Long): Flow<List<FolderWithRoutines>>
//
//    @Query("SELECT * FROM folders WHERE folderId = :id")
//    suspend fun getFolderById(id: Long): FolderEntity?
//
//    @Query("SELECT EXISTS(SELECT 1 FROM folders WHERE userId = :userId AND name = :name LIMIT 1)")
//    suspend fun existsByName(userId: Long, name: String): Boolean
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(folder: FolderEntity): Long
//
//    @Delete
//    suspend fun deleteFolder(folder: FolderEntity)
//}
//

package com.voltfitness.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.voltfitness.app.data.local.entities.FolderEntity
import com.voltfitness.app.data.local.relations.FolderWithRoutines
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertFolder(folder: FolderEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(folder: FolderEntity): Long

    @Update
    suspend fun update(folder: FolderEntity)

    @Delete
    suspend fun delete(folder: FolderEntity)

    @Query("SELECT * FROM folders WHERE folderId = :id")
    suspend fun getById(id: Long): FolderEntity?

    @Query("SELECT * FROM folders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getFoldersFlow(userId: Long): Flow<List<FolderEntity>>

    @Transaction
    @Query("SELECT * FROM folders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getFoldersWithRoutinesFlow(userId: Long): Flow<List<FolderWithRoutines>>

    @Query("SELECT EXISTS(SELECT 1 FROM folders WHERE userId = :userId AND name = :name LIMIT 1)")
    suspend fun existsByName(userId: Long, name: String): Boolean
}

