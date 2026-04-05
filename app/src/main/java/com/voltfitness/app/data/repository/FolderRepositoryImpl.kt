package com.voltfitness.app.data.repository

import com.voltfitness.app.data.local.dao.FolderDao
import com.voltfitness.app.data.mappers.toDomain
import com.voltfitness.app.data.mappers.toEntity
import com.voltfitness.app.domain.model.Folder
import com.voltfitness.app.domain.relations.FolderWithRoutinesDomain
import com.voltfitness.app.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Repository implementation for managing exercise folders and their organizational structure.
 * Acts as the bridge between [FolderDao] and the domain layer.
 */
class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao
) : FolderRepository {

    /**
     * Observes all folders belonging to a specific user.
     */
    override fun getFoldersByUser(userId: Long): Flow<List<Folder>> =
        folderDao.getFoldersFlow(userId).map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * Observes a list of folders including their nested routine data.
     * Maps database relation objects to domain relation models.
     */
    override fun getFoldersWithRoutines(userId: Long): Flow<List<FolderWithRoutinesDomain>> =
        folderDao.getFoldersWithRoutinesFlow(userId).map { list ->
            list.map { it.toDomain() }
        }

    /**
     * Retrieves a single folder by its unique identifier.
     */
    override suspend fun getById(folderId: Long): Folder? =
        folderDao.getById(folderId)?.toDomain()

    /**
     * Validates if a folder name already exists for a user to prevent duplicates.
     */
    override suspend fun existsByName(userId: Long, name: String): Boolean =
        folderDao.existsByName(userId, name)

    /**
     * Creates a new folder or updates an existing one if the ID matches.
     *
     * @return The row ID of the inserted or updated folder.
     */
    override suspend fun upsertFolder(folder: Folder): Long =
        folderDao.upsertFolder(folder.toEntity())

    /**
     * Removes a folder from the database.
     * Note: Cascading behavior for routines depends on the database schema configuration.
     */
    override suspend fun delete(folder: Folder) =
        folderDao.delete(folder.toEntity())
}