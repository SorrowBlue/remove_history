package com.sorrowblue.comicviewer.domain.model.file

import com.sorrowblue.comicviewer.domain.model.bookshelf.BookshelfId

data class Folder(
    override val bookshelfId: BookshelfId,
    override val name: String,
    override val parent: String,
    override val path: String,
    override val size: Long,
    override val lastModifier: Long,
    override val params: Map<String, String?> = emptyMap(),
    override val count: Int = 0,
    override val sortIndex: Int = -1,
) : IFolder {

    override fun areContentsTheSame(file: File): Boolean {
        return if (file is Folder) {
            bookshelfId == file.bookshelfId && path == file.path
        } else {
            false
        }
    }
}
