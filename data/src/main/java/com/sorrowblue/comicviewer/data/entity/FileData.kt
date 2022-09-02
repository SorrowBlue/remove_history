package com.sorrowblue.comicviewer.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.sorrowblue.comicviewer.domain.model.file.Book
import com.sorrowblue.comicviewer.domain.model.file.Bookshelf
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.domain.model.library.LibraryId
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime

private const val LIBRARY_ID = "library_id"

@Entity(
    tableName = "file",
    primaryKeys = ["path", LIBRARY_ID],
    foreignKeys = [ForeignKey(
        entity = LibraryData::class,
        parentColumns = ["id"],
        childColumns = [LIBRARY_ID],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = [LIBRARY_ID])]
)
data class FileData(
    val path: String,
    val name: String,
    val parent: String,
    @ColumnInfo(name = LIBRARY_ID) val libraryId: Int,
    val fileSize: Long,
    val timestamp: LocalDateTime,
    val preview: String,
    val pageHistory: Int,
    val maxPage: Int,
    @ColumnInfo(name = "is_file") val isFile: Boolean,
) {

    fun toFile(previewList: List<String> = emptyList()): File {
        return if (isFile) {
            Book(
                name,
                parent,
                path,
                name.split(".")[1],
                fileSize,
                timestamp,
                preview,
                pageHistory,
                maxPage
            )
        } else {
            Bookshelf(
                name,
                parent,
                path,
                "",
                fileSize,
                timestamp,
                previewList
            )
        }
    }

    val previewName
        get() : String {
            val md5 = MessageDigest.getInstance("SHA-224")
            val bytes = md5.digest((path + timestamp).toByteArray())
            return String.format("%020x", BigInteger(1, bytes))
        }
}

internal fun File.toData(libraryId: LibraryId): FileData {
    return when (this) {
        is Book -> FileData(
            path = path,
            name = name,
            parent = parent,
            libraryId = libraryId.value,
            fileSize = fileSize,
            timestamp = timestamp,
            preview = preview,
            pageHistory = pageHistory,
            maxPage = maxPage,
            isFile = true
        )
        is Bookshelf -> FileData(
            path = path,
            name = name,
            parent = parent,
            libraryId = libraryId.value,
            fileSize = fileSize,
            timestamp = timestamp,
            preview = "",
            pageHistory = 0,
            maxPage = 0,
            isFile = false
        )
    }
}
