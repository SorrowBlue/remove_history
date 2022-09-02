package com.sorrowblue.comicviewer.domain.model.settings

import com.sorrowblue.comicviewer.domain.model.Display
import com.sorrowblue.comicviewer.domain.model.SupportExtension
import kotlinx.serialization.Serializable

@Serializable
data class BookshelfSettings(
    val display: Display = Display.GRID,
    val sort: Sort = Sort.NAME,
    val order: Order = Order.ASC,
    val supportExtension: Set<SupportExtension> = setOf(SupportExtension.ZIP),
    ) {

    enum class Order {
        ASC,
        DESC
    }

    enum class Sort {
        NAME,
        DATE,
        SIZE
    }
}
