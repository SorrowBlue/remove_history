package com.sorrowblue.comicviewer.domain.model.file

import android.os.Parcelable
import java.io.Serializable
import java.time.LocalDateTime
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface File : Parcelable, Serializable {
    val name: String
    val parent: String
    val path: String
    val extension: String
    val fileSize: Long
    val timestamp: LocalDateTime
}
