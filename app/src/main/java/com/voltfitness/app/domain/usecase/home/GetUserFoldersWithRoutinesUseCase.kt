package com.voltfitness.app.domain.usecase.home

import com.voltfitness.app.domain.relations.FolderWithRoutinesDomain
import com.voltfitness.app.domain.repository.FolderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observes all folders with their nested routines for the home screen.
 * Delegates to [FolderRepository] which is the aggregate root owner.
 */
class GetUserFoldersWithRoutinesUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    operator fun invoke(userId: Long): Flow<List<FolderWithRoutinesDomain>> =
        folderRepository.getFoldersWithRoutines(userId)
}
