package com.voltfitness.app.domain.usecase.folder

import com.voltfitness.app.domain.model.Folder
import com.voltfitness.app.domain.repository.FolderRepository
import javax.inject.Inject

/**
 * Updates an existing folder's metadata (name, description, color).
 */
class UpdateFolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(folder: Folder) {
        folderRepository.upsertFolder(
            folder.copy(updatedAt = System.currentTimeMillis())
        )
    }
}