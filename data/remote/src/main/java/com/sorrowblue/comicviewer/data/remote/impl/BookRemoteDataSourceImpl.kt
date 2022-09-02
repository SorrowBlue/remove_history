package com.sorrowblue.comicviewer.data.remote.impl

import android.content.Context
import com.sorrowblue.comicviewer.data.datasource.BookRemoteDataSource
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

internal class BookRemoteDataSourceImpl @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted private val libraryData: LibraryData,
    @Assisted private val fileData: FileData,
) : BookRemoteDataSource {

    @AssistedFactory
    interface Factory : BookRemoteDataSource.Factory {
        override fun create(libraryData: LibraryData, fileData: FileData): BookRemoteDataSourceImpl
    }

    private val fileClient = when (libraryData.protocol) {
        "SMB" -> SmbFileClient(libraryData)
        "LOCAL" -> LocalFileClient(context, libraryData)
        else -> TODO()
    }

    private val fileReader = when (fileData.path.extension) {
        "zip" -> ZipFileReader(fileClient, libraryData, fileData)
        "pdf" -> pdfFileReader(fileData)
        else -> TODO()
    }

    override fun count() = fileReader.pageCount()
    override fun pageInputStream(pageIndex: Int) = fileReader.pageInputStream(pageIndex)

    override fun close() = fileReader.close()


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
