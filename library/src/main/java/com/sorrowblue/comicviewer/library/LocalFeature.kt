package com.sorrowblue.comicviewer.library

enum class LocalFeature : Library {
    HISTORY,
    DOWNLOADED;

    override val viewType = LibraryListViewType.LOCAL_FEATURE
}
