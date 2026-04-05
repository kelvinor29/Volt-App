package com.voltfitness.app.domain.repository

import com.voltfitness.app.domain.model.Folder
import com.voltfitness.app.domain.relations.FolderWithRoutinesDomain
import kotlinx.coroutines.flow.Flow

/**
 * Repository contract for Folder CRUD operations.
 *
 * This is the single source of truth for folder data management,
 * following the Single Responsibility Principle.
 * Cross-entity queries (folder + routines) live here because
 * the folder is the aggregate root of the relationship.
 */
interface FolderRepository {

    /** Observes all folders for a user, ordered by creation date. */
    fun getFoldersByUser(userId: Long): Flow<List<Folder>>

    /** Observes folders with their nested routines for the home screen. */
    fun getFoldersWithRoutines(userId: Long): Flow<List<FolderWithRoutinesDomain>>

    /** Retrieves a single folder by ID, or null if not found. */
    suspend fun getById(folderId: Long): Folder?

    /** Checks if a folder with the given name already exists for a user. */
    suspend fun existsByName(userId: Long, name: String): Boolean

    /** Upsert a folder (ignores on conflict). Returns the generated ID. */
    suspend fun upsertFolder(folder: Folder): Long

    /** Deletes a folder and all its routines (via CASCADE). */
    suspend fun delete(folder: Folder)
}
