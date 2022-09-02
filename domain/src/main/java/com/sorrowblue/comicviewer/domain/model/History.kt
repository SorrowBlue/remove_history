package com.sorrowblue.comicviewer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class History(val bookshelfId: Int = 0, val currentComic: String? = null)

enum class Display {
    GRID, LIST
}


