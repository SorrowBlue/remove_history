package com.sorrowblue.comicviewer.data.remote.archive

import android.util.Log
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.remote.communication.FileClient
import java.io.InputStream
import java.nio.channels.SeekableByteChannel
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipFile

internal val SUPPORTED_IMAGE = setOf("bmp",
    "gif",
    "jpg",
    "jpeg",
    "png",
    "webp",
    "heic",
    "heif")

internal class ZipFileReader(
    override val client: FileClient,
    override val libraryData: LibraryData,
    override val fileData: FileData,
) : FileReader {

    private var _seekableByteChannel: SeekableByteChannel? = null
    private val seekableByteChannel
        get() = _seekableByteChannel?.let { if (it.isOpen) it else null } ?: kotlin.run {
            client.seekableByteChannel(fileData).also { _seekableByteChannel = it }
        }

    private val zipFile = ZipFile(seekableByteChannel, "Shift_JIS")

    private val entries by lazy {
        zipFile.entries.toList().filter {
            !it.isDirectory && it.name.extension in SUPPORTED_IMAGE
        }.sortedWith(compareBy<ZipArchiveEntry> { it.name.length }.thenBy { it.name })
    }

    override fun pageInputStream(pageIndex: Int): InputStream {
        val entry = entries[pageIndex]
        return zipFile.getInputStream(entry)
    }
    override var quality = 75

    override fun pageCount(): Int {
        return entries.size
    }

    override fun close() {
        seekableByteChannel.close()
        zipFile.close()
    }
}

internal val String.extension get() = substringAfterLast(".")
