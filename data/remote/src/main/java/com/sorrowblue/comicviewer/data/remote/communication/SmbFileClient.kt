package com.sorrowblue.comicviewer.data.remote.communication

import android.net.Uri
import androidx.core.net.toUri
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.remote.SmbSeekableByteChannel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.io.InputStream
import java.nio.channels.SeekableByteChannel
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Properties
import jcifs.CIFSContext
import jcifs.config.PropertyConfiguration
import jcifs.context.BaseContext
import jcifs.smb.NtlmPasswordAuthenticator
import jcifs.smb.SmbFile

internal class SmbFileClient @AssistedInject constructor(
    @Assisted override val library: LibraryData,
) : FileClient {

    @AssistedFactory
    interface Factory : FileClient.Factory {
        override fun create(libraryData: LibraryData): SmbFileClient
    }

    override fun seekableByteChannel(fileData: FileData): SeekableByteChannel {
        return SmbSeekableByteChannel(
            smbFile(fileData),
            write = false,
            create = false,
            create_new = false,
            truncate = false,
            append = false
        )
    }

    override suspend fun exists(fileData: FileData): Boolean {
        return smbFile(fileData).exists()
    }

    override fun inputStream(fileData: FileData): InputStream = smbFile(fileData).inputStream

    override suspend fun listFiles(
        fileData: FileData?,
        filter: (Boolean, String) -> Boolean
    ): List<FileData> {
        return smbFile(fileData).use {
            it.listFiles { it -> filter.invoke(it.isFile, it.name) }
        }.map { smbFile ->
            smbFile.use {
                FileData(
                    path = it.path.toUri().path.orEmpty(),
                    name = it.name.removeSuffix("/"),
                    parent = it.parent.toUri().path.orEmpty(),
                    libraryId = library.id,
                    fileSize = it.length(),
                    timestamp = LocalDateTime.ofEpochSecond(it.lastModified, 0, ZoneOffset.UTC),
                    preview = "",
                    maxPage = 0,
                    pageHistory = 0,
                    isFile = it.isFile
                )
            }
        }
    }

    private fun smbFile(fileData: FileData?): SmbFile {
        val uri = Uri.Builder()
            .scheme("smb")
            .authority(library.host)
            .path(fileData?.path ?: library.path)
            .build().toString().let {
                Uri.decode(it)
            }
        return SmbFile(uri, cifsContext())
    }

    private fun cifsContext(): CIFSContext {
        val prop = Properties().apply {
            setProperty("jcifs.smb.client.minVersion", "SMB210")
            setProperty("jcifs.smb.client.maxVersion", "SMB300")
            setProperty("jcifs.smb.client.responseTimeout", "3000")
            setProperty("jcifs.smb.client.soTimeout", "3000")
            setProperty("jcifs.smb.client.connTimeout", "3000")
            setProperty("jcifs.smb.client.sessionTimeout", "3000")
        }
        val context = BaseContext(PropertyConfiguration(prop))
        return if (library.username.isEmpty()) {
            context.withAnonymousCredentials()
        } else {
            context.withCredentials(
                NtlmPasswordAuthenticator(library.host, library.username, library.password)
            )
        }
    }
}

