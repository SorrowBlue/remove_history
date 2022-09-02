package com.sorrowblue.comicviewer.data.remote.communication

import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import java.io.InputStream
import java.nio.channels.SeekableByteChannel

interface FileClient {
    val library: LibraryData
    suspend fun listFiles(fileData: FileData?, filter: (Boolean, String) -> Boolean): List<FileData>
    suspend fun exists(fileData: FileData): Boolean
    fun seekableByteChannel(fileData: FileData): SeekableByteChannel
    fun inputStream(fileData: FileData): InputStream?

    interface Factory {

        fun create(libraryData: LibraryData): FileClient
    }
}
