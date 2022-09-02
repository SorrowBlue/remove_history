package com.sorrowblue.comicviewer.domain.model.page

import android.os.Parcelable
import java.io.Serializable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Page(
    val index: Int,
    val preview: String?,
) : Parcelable, Serializable
