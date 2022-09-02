package com.sorrowblue.comicviewer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.model.library.SupportProtocol
import com.sorrowblue.comicviewer.domain.model.page.Page

@Entity(tableName = "library_data")
data class LibraryData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val host: String,
    val path: String,
    val port: String,
    val protocol: String,
    val username: String,
    val password: String,
) {
    fun toLibrary(preview: List<String> = emptyList()) =
        Library(id, name, host, path, port, SupportProtocol.valueOf(protocol), username, password, preview)
}

internal fun Library.toData(): LibraryData {
    return LibraryData(id.value, name, host, path, port, protocol.name, username, password)
}

@Entity(tableName = "page_data")
class PageData(
    @PrimaryKey
    val index: Int,
    val preview: String?,
) {
    fun toPage() = Page(index, preview)

    companion object {
        fun fromPage(page: Page): PageData {
            return page.run {
                PageData(index, preview)
            }
        }
    }
}
