package com.sorrowblue.comicviewer.domain.model.file

import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bookshelf(
    override val name: String,
    override val parent: String,
    override val path: String,
    override val extension: String,
    override val fileSize: Long,
    override val timestamp: LocalDateTime,
    val preview: List<String>
) : File
