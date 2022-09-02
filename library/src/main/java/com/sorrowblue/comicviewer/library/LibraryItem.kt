package com.sorrowblue.comicviewer.library

import com.sorrowblue.comicviewer.domain.model.library.Library

data class LibraryItem(
    val library: Library,
    val name: String,
    val preview: List<String>
)
