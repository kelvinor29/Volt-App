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

class FolderRepositoryImpl @Inject constructor(
    private val folderDao: FolderDao
) : FolderRepository {

    override fun getFoldersByUser(userId: Long): Flow<List<Folder>> =
        folderDao.getFoldersFlow(userId).map { entities ->
            entities.map { it.toDomain() }
        }

    override fun getFoldersWithRoutines(userId: Long): Flow<List<FolderWithRoutinesDomain>> =
        folderDao.getFoldersWithRoutinesFlow(userId).map { relations ->
            relations.map { relation ->
                FolderWithRoutinesDomain(
                    folder = relation.folder.toDomain(),
                    routines = relation.routines.map { it.toDomain() }
                )
            }
        }

    override suspend fun getById(folderId: Long): Folder? =
        folderDao.getById(folderId)?.toDomain()

    override suspend fun existsByName(userId: Long, name: String): Boolean =
        folderDao.existsByName(userId, name)

    override suspend fun insert(folder: Folder): Long =
        folderDao.insert(folder.toEntity())

    override suspend fun update(folder: Folder) =
        folderDao.update(folder.toEntity())

    override suspend fun delete(folder: Folder) =
        folderDao.delete(folder.toEntity())
}
