package com.sorrowblue.comicviewer.domain.entity.settings

import kotlinx.serialization.Serializable

@Serializable
data class FolderDisplaySettings(
    val display: Display = Display.GRID,
    val isEnabledThumbnail: Boolean = true,
    val spanCount: Int = 3,
    val columnSize: Size = Size.MEDIUM,
    val sort: Sort = Sort.NAME,
    val order: Order = Order.ASC
) {

    enum class Size {
        SMALL, MEDIUM, LARGE
    }

    enum class Display {
        GRID, LIST
    }

    enum class Order {
        ASC, DESC
    }

    enum class Sort {
        NAME, DATE, SIZE
    }

    val rawSpanCount
        get() = when (display) {
            Display.GRID -> spanCount
            Display.LIST -> 1
        }
}
