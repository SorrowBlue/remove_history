package com.sorrowblue.comicviewer.data.reporitory

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import com.sorrowblue.comicviewer.data.datasource.BookRemoteDataSource
import com.sorrowblue.comicviewer.data.entity.toData
import com.sorrowblue.comicviewer.domain.model.file.Book
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.domain.repository.BookRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.outputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BookRepositoryImpl @AssistedInject constructor(
    @Assisted private val library: Library,
    @Assisted private val book: Book,
    @ApplicationContext private val context: Context,
    val factory: BookRemoteDataSource.Factory,
) : BookRepository {

    private var bookRemoteDataSource: BookRemoteDataSource? = null

    private suspend fun bookRemoteDataSource() =
        bookRemoteDataSource ?: factory.create(library.toData(), book.toData(library.id))
            .also { bookRemoteDataSource = it }

    @AssistedFactory
    interface Factory : BookRepository.Factory {
        override fun create(library: Library, book: Book): BookRepositoryImpl
    }

    override suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            context.cacheDir.toPath().resolve("zip").resolve(book.toData(library.id).previewName)
                .apply {
                    createDirectories()
                    listDirectoryEntries().forEach { it.deleteIfExists() }
                }
        }
    }

    override suspend fun pageCount(): Int {
        return bookRemoteDataSource().count()
    }

    override suspend fun loadPage(page: Int): String {
        val pagePath =
            context.cacheDir.toPath().resolve("zip").resolve(book.toData(library.id).previewName)
                .resolve("$page.webp")
        bookRemoteDataSource().pageInputStream(page).use { input ->
            pagePath.outputStream().use {
                BitmapFactory.decodeStream(input)
                    .compress(COMPRESS_FORMAT, 30, it)
            }
        }
        return pagePath.toString()
    }

    override suspend fun close() {
        withContext(Dispatchers.IO) {
            bookRemoteDataSource?.close()
        }
    }
}

private val COMPRESS_FORMAT =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSY else Bitmap.CompressFormat.JPEG
