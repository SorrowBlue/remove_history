package com.sorrowblue.comicviewer.data.reporitory

import com.sorrowblue.comicviewer.domain.model.Response
import java.io.InputStream
import java.nio.channels.SeekableByteChannel

interface ComicClient {
    fun seekableByteChannel(): SeekableByteChannel
    suspend fun exists2(): Response<Boolean>

    val inputStream: InputStream
}
