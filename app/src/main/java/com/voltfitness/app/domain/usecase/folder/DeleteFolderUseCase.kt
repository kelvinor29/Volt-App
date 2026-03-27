package com.voltfitness.app.domain.usecase.folder

import com.voltfitness.app.domain.repository.FolderRepository
import javax.inject.Inject

/**
 * Deletes a folder and all its nested routines via CASCADE.
 * The caller should confirm this destructive action with the user first.
 */
class DeleteFolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    suspend operator fun invoke(folderId: Long) {
        val folder = folderRepository.getById(folderId) ?: return
        folderRepository.delete(folder)
    }
}
