package com.sorrowblue.comicviewer.data.pdf

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.ParcelFileDescriptor
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.remote.archive.FileReader
import com.sorrowblue.comicviewer.data.remote.communication.FileClient
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.rendering.ImageType
import com.tom_roush.pdfbox.rendering.PDFRenderer
import java.io.File
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.notExists

private val COMPRESS_FORMAT =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Bitmap.CompressFormat.WEBP_LOSSY else Bitmap.CompressFormat.JPEG

internal class PdfFileReader(
    override val client: FileClient,
    override val libraryData: LibraryData,
    override val fileData: FileData,
    context: Context,
) : FileReader {

    private val input = client.inputStream(fileData)

    private val document = PDDocument.load(input)

    override fun pageCount(): Int {
        return document.numberOfPages
    }

    override var quality = 75

    private val pdfCache = context.cacheDir.toPath().resolve("pdf-tmp")

    init {
        if (pdfCache.notExists()) {
            pdfCache.createDirectories()
        }
        pdfCache.deleteChildFile()
    }


    override fun pageInputStream(pageIndex: Int): InputStream {
        val bitmap = PDFRenderer(document).renderImageWithDPI(pageIndex, 160f, ImageType.RGB)
        return File.createTempFile("pdfCache", ".png", pdfCache.toFile()).let {
            it.outputStream().use { bitmap.compress(COMPRESS_FORMAT, quality, it) }
            it.inputStream()
        }
    }

    override fun close() {
        input?.close()
        document.close()
    }

    private fun Path.deleteChildFile() {
        if (isDirectory()) {
            listDirectoryEntries().forEach { it.deleteChildFile() }
        }
    }
}
