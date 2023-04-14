package com.sorrowblue.comicviewer.domain.usecase.paging

import androidx.paging.PagingConfig
import com.sorrowblue.comicviewer.domain.entity.SearchCondition
import com.sorrowblue.comicviewer.domain.entity.bookshelf.Bookshelf
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.request.BaseRequest
import kotlinx.serialization.Serializable

abstract class PagingQueryFileUseCase : PagingUseCase<PagingQueryFileUseCase.Request, File>() {

    class Request(
        val pagingConfig: PagingConfig,
        val bookshelf: Bookshelf,
        val searchCondition: SearchCondition,
        val sortType: () -> SortType
    ) : BaseRequest {

        override fun validate(): Boolean {
            return true
        }
    }
}

@Serializable
sealed interface SortType {

    val isAsc: Boolean


    fun copy2(isAsc: Boolean): SortType {
        return when (this) {
            is DATE -> copy(isAsc)
            is NAME -> copy(isAsc)
            is SIZE -> copy(isAsc)
        }
    }

    @Serializable
    data class NAME(override val isAsc: Boolean) : SortType

    @Serializable
    data class DATE(override val isAsc: Boolean) : SortType

    @Serializable
    data class SIZE(override val isAsc: Boolean) : SortType
}
