package com.sorrowblue.comicviewer.data.remote.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import com.sorrowblue.comicviewer.data.datasource.FileRemoteDataSource
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.remote.archive.FileReader
import com.sorrowblue.comicviewer.data.remote.archive.ZipFileReader
import com.sorrowblue.comicviewer.data.remote.archive.extension
import com.sorrowblue.comicviewer.data.remote.communication.FileClient
import com.sorrowblue.comicviewer.data.remote.communication.LocalFileClient
import com.sorrowblue.comicviewer.data.remote.communication.SmbFileClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

internal class FileRemoteDataSourceImpl @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val libraryData: LibraryData,
) : FileRemoteDataSource {

    @AssistedFactory
    interface Factory : FileRemoteDataSource.Factory {
        override fun create(libraryData: LibraryData): FileRemoteDataSourceImpl
    }

    private val fileClient = when (libraryData.protocol) {
        "SMB" -> SmbFileClient(libraryData)
        "local" -> LocalFileClient(context, libraryData)
        else -> TODO()
    }

    override suspend fun listFiles(fileData: FileData?): List<FileData> {
        return fileClient.listFiles(fileData) { isFile, name -> !isFile || name.extension in listOf("zip") }
    }

    override suspend fun getPreview(
        list: List<FileData>,
        done: suspend (FileData) -> Unit,
    ) {
        if (list.isEmpty()) return
        withContext(Dispatchers.IO) {
            var i = 0
            val lastIndex = list.lastIndex
            val list1 = list.sortedBy { it.name }
            List(Integer.min(list1.size, 8)) {
                async {
                    while (i <= lastIndex) {
                        val oldData = list1[i++]
                        val newData = update(oldData)
                        done.invoke(newData)
                    }
                }
            }.awaitAll()
        }
    }

    private fun update(fileData: FileData): FileData {
        if (File(fileData.path).extension in listOf("pdf", "zip")) {
            val cacheDir = context.cacheDir.toPath()
            val coversCache =
                cacheDir.resolve("covers").resolve(fileData.previewName)
            if (!coversCache.exists()) {
                coversCache.parent.createDirectories()
            }
            val wrapper: FileReader? =
                kotlin.runCatching {
                    when (File(fileData.path).extension) {
                        "pdf" -> pdfFileReader(fileData)
                        "zip" -> ZipFileReader(fileClient, libraryData, fileData)
                    else -> null
                }
                }.onFailure {
                    Log.d("APPAPP", "error path=${fileData.path}")
                    it.printStackTrace()
                }.getOrNull()
            return wrapper?.use {
                it.pageInputStream(0).use { input ->
                    coversCache.outputStream().use {
                        BitmapFactory.decodeStream(input)
                            .compress(COMPRESS_FORMAT, 30, it)
                    }
                }
                fileData.copy(maxPage = it.pageCount(), preview = coversCache.toString())
            } ?: fileData
        } else {
            return fileData
        }
    }

    fun pdfFileReader(fileData: FileData) =
        Class.forName("com.sorrowblue.comicviewer.data.pdf.PdfFileReader")
            .getConstructor(
                FileClient::class.java,
                LibraryData::class.java,
                FileData::class.java,
                Context::class.java
            )
            .newInstance(fileClient, libraryData, fileData, context) as FileReader
}

private val COMPRESS_FORMAT =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSY else Bitmap.CompressFormat.JPEG
