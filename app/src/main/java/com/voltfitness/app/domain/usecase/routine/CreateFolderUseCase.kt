package com.voltfitness.app.domain.usecase.folder

import com.voltfitness.app.domain.model.Folder
import com.voltfitness.app.domain.repository.FolderRepository
import javax.inject.Inject

/**
 * Creates a new folder for organizing routines.
 * Validates that no folder with the same name exists for the user
 * before inserting.
 *
 * @return The generated folder ID, or -1 if a duplicate name exists.
 */
class CreateFolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(userId: Long, name: String, description: String? = null): Long {
        if (folderRepository.existsByName(userId, name)) return -1L

        return folderRepository.upsertFolder(
            Folder(
                id = 0L,
                userId = userId,
                name = name,
                description = description,
                colorHex = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = null
            )
        )
    }
}
