package com.sorrowblue.comicviewer.domain.entity

data class SearchCondition(
    val query: String?,
    val range: Range,
    val period: Period
) {

    sealed class Range {
        data object BOOKSHELF : Range()
        data class IN_FOLDER(val parent: String) : Range()
        data class FOLDER_BELOW(val parent: String) : Range()
    }

    enum class Period {
        NONE,
        HOUR_24,
        WEEK_1,
        MONTH_1
    }
}

data class SearchCondition2(
    val query: String,
    val range: Range,
    val period: Period,
    val order: Order,
    val sort: Sort
) {

    sealed class Range {
        data object BOOKSHELF : Range()
        data class InFolder(val parent: String) : Range()
        data class FolderBelow(val parent: String) : Range()
    }

    enum class Period {
        NONE, HOUR_24, WEEK_1, MONTH_1
    }

    enum class Order {
        NAME, DATE, SIZE
    }

    enum class Sort {
        ASC, DESC
    }
}
