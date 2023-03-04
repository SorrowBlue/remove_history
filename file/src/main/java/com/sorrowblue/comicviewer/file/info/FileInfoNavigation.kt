package com.sorrowblue.comicviewer.file.info

import androidx.core.net.toUri
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.framework.ui.fragment.encodeBase64

object FileInfoNavigation {

    fun getDeeplink(file: File) = "comicviewer://comicviewer.sorrowblue.com/file_info?server_id=${file.bookshelfId.value}&path=${file.path.encodeBase64()}".toUri()
}
