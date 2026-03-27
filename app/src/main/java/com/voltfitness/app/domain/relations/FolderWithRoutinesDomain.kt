package com.voltfitness.app.domain.relations

import com.voltfitness.app.domain.model.Folder
import com.voltfitness.app.domain.model.Routine

data class FolderWithRoutinesDomain(
    val folder: Folder,
    val routines: List<Routine>
)