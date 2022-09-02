package com.sorrowblue.comicviewer.data.remote.archive

import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.remote.communication.FileClient
import java.io.Closeable
import java.io.InputStream

interface FileReader : Closeable {
    var quality: Int
    val client: FileClient
    val libraryData: LibraryData
    val fileData: FileData
    fun pageInputStream(pageIndex: Int): InputStream
    fun pageCount(): Int
}
