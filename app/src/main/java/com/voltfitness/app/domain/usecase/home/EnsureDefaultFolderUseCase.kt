package com.voltfitness.app.domain.usecase.home

import com.voltfitness.app.domain.model.Folder
import com.voltfitness.app.domain.repository.FolderRepository
import javax.inject.Inject

/**
 * Ensures that a default "My Routines" folder exists for the given user.
 *
 * This use case is idempotent: if the folder already exists, it performs
 * no operation. This avoids duplicate entries and is safe to call on every
 * app launch from the HomeViewModel.
 *
 * @see FolderRepository
 */
class EnsureDefaultFolderUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    companion object {
        const val DEFAULT_FOLDER_NAME = "My Routines"
    }

    /**
     * @param userId The ID of the current user.
     */
    suspend operator fun invoke(userId: Long) {
        val exists = folderRepository.existsByName(userId, DEFAULT_FOLDER_NAME)
        if (!exists) {
            folderRepository.upsertFolder(
                Folder(
                    id = 0L,
                    userId = userId,
                    name = DEFAULT_FOLDER_NAME,
                    description = "Your default routine collection",
                    colorHex = null,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = null
                )
            )
        }
    }
}
