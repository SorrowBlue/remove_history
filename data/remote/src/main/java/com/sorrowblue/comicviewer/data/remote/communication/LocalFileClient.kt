package com.sorrowblue.comicviewer.data.remote.communication

import android.content.Context
import android.os.ParcelFileDescriptor
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.InputStream
import java.nio.channels.SeekableByteChannel
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class LocalFileClient @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted override val library: LibraryData,
) : FileClient {

    @AssistedFactory
    interface Factory : FileClient.Factory {
        override fun create(libraryData: LibraryData): LocalFileClient
    }

    override fun seekableByteChannel(fileData: FileData): SeekableByteChannel {
        val pfd = context.contentResolver.openFileDescriptor(uri(fileData), "r")
        val stream = ParcelFileDescriptor.AutoCloseInputStream(pfd)
        return stream.channel
    }

    override suspend fun exists(fileData: FileData): Boolean {
        return document(fileData)?.exists() ?: false
    }

    override fun inputStream(fileData: FileData): InputStream? =
        context.contentResolver.openInputStream(uri(fileData))

    override suspend fun listFiles(
        fileData: FileData?,
        filter: (Boolean, String) -> Boolean,
    ): List<FileData> {
        return document(fileData)?.listFiles().orEmpty().filter {
            filter.invoke(it.isFile, it.name.orEmpty())
        }.map {
            FileData(
                path = it.uri.encodedPath.orEmpty(),
                name = it.name?.removeSuffix("/").orEmpty(),
                parent = it.parentFile?.uri?.encodedPath.orEmpty(),
                libraryId = library.id,
                fileSize = it.length(),
                timestamp = LocalDateTime.ofEpochSecond(it.lastModified(), 0, ZoneOffset.UTC),
                preview = "",
                maxPage = 0,
                pageHistory = 0,
                isFile = it.isFile
            )
        }
    }

    private fun uri(fileData: FileData?) =
        "content://${library.host}${fileData?.path ?: library.path}".toUri()

    private fun document(fileData: FileData?): DocumentFile? =
        DocumentFile.fromTreeUri(context, uri(fileData))
}
