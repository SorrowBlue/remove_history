package com.sorrowblue.comicviewer.feature.search.section

import android.os.Parcelable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sorrowblue.comicviewer.feature.search.component.DropdownMenuChip
import com.sorrowblue.comicviewer.framework.ui.material3.TopAppBarScrollBehavior
import kotlinx.collections.immutable.toPersistentList
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class SearchConditionsUiState(
    val range: Range = Range.BOOKSHELF,
    val period: Period = Period.NONE,
    val order: Order = Order.NAME,
    val sort: Sort = Sort.ASC,
) : Parcelable {

    sealed interface SearchCondition
    enum class Range : SearchCondition {
        BOOKSHELF, IN_FOLDER, FOLDER_BELOW
    }

    enum class Period : SearchCondition {
        NONE, HOUR_24, WEEK_1, MONTH_1
    }

    enum class Order : SearchCondition {
        NAME, TIMESTAMP, SIZE
    }

    enum class Sort : SearchCondition {
        ASC, DESC
    }
}

@Composable
internal fun SearchConditions(
    uiState: SearchConditionsUiState,
    onChangeSearchCondition: (SearchConditionsUiState.SearchCondition) -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Row(
        Modifier
            .horizontalScroll(rememberScrollState())
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DropdownMenuChip(
            text = uiState.range.name,
            onChangeSelected = onChangeSearchCondition,
            menus = remember { SearchConditionsUiState.Range.entries.toPersistentList() },
            menu = SearchConditionsUiState.Range::name,
            scrollBehavior = scrollBehavior
        )
        DropdownMenuChip(
            text = uiState.period.name,
            onChangeSelected = onChangeSearchCondition,
            menus = remember { SearchConditionsUiState.Period.entries.toPersistentList() },
            menu = SearchConditionsUiState.Period::name,
            scrollBehavior = scrollBehavior
        )
        DropdownMenuChip(
            text = uiState.order.name,
            onChangeSelected = onChangeSearchCondition,
            menus = remember { SearchConditionsUiState.Order.entries.toPersistentList() },
            menu = SearchConditionsUiState.Order::name,
            scrollBehavior = scrollBehavior
        )
        DropdownMenuChip(
            text = uiState.sort.name,
            onChangeSelected = onChangeSearchCondition,
            menus = remember { SearchConditionsUiState.Sort.entries.toPersistentList() },
            menu = SearchConditionsUiState.Sort::name,
            scrollBehavior = scrollBehavior
        )
    }
}
