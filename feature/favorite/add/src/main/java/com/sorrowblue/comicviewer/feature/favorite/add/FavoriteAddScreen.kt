package com.sorrowblue.comicviewer.feature.favorite.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.sorrowblue.comicviewer.domain.model.favorite.Favorite
import com.sorrowblue.comicviewer.feature.favorite.add.component.FavoriteAddFab
import com.sorrowblue.comicviewer.feature.favorite.add.component.FavoriteAddTopAppBar
import com.sorrowblue.comicviewer.feature.favorite.common.component.FavoriteCreateDialog
import com.sorrowblue.comicviewer.feature.favorite.common.component.FavoriteCreateDialogUiState
import com.sorrowblue.comicviewer.feature.favorite.common.component.FavoriteItem
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons
import com.sorrowblue.comicviewer.framework.designsystem.theme.ComicTheme
import com.sorrowblue.comicviewer.framework.ui.add
import com.sorrowblue.comicviewer.framework.ui.asWindowInsets
import com.sorrowblue.comicviewer.framework.ui.preview.rememberMobile
import com.sorrowblue.comicviewer.feature.favorite.common.R as FavoriteCommonR

context(NavBackStackEntry)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoriteAddRoute(
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    state: FavoriteAddScreenState = rememberFavoriteAddScreenState(),
) {
    val dialogUiState = state.dialogUiState
    val lazyPagingItems = state.pagingDataFlow.collectAsLazyPagingItems()
    if (rememberMobile()) {
        FavoriteAddScreen(
            lazyPagingItems = lazyPagingItems,
            onBackClick = onBackClick,
            onFavoriteClick = state::onFavoriteClick,
            onAddClick = state::onNewFavoriteClick,
            contentPadding = contentPadding
        )
        FavoriteCreateDialog(
            uiState = dialogUiState,
            onNameChange = state::onNameChange,
            onDismissRequest = state::onDismissRequest,
            onCreateClick = state::onCreateClick
        )
    } else {
        FavoriteAddDialog(
            uiState = dialogUiState,
            lazyPagingItems = lazyPagingItems,
            onDismissRequest = onBackClick,
            onFavoriteClick = state::onFavoriteClick,
            onAddClick = state::onNewFavoriteClick,
            onNameChange = state::onNameChange,
            onSaveClick = state::onCreateClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FavoriteAddScreen(
    lazyPagingItems: LazyPagingItems<Favorite>,
    onBackClick: () -> Unit,
    onFavoriteClick: (Favorite) -> Unit,
    onAddClick: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    lazyListState: LazyListState = rememberLazyListState(),
    appBarScrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    Scaffold(
        topBar = { FavoriteAddTopAppBar(onBackClick, appBarScrollBehavior) },
        floatingActionButton = { FavoriteAddFab(onAddClick) },
        contentWindowInsets = contentPadding.asWindowInsets(),
        modifier = Modifier.nestedScroll(appBarScrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        FavoriteAddContent(
            lazyListState = lazyListState,
            innerPadding = innerPadding.add(paddingValues = PaddingValues(bottom = 88.dp)),
            lazyPagingItems = lazyPagingItems,
            onFavoriteClick = onFavoriteClick
        )
    }

}

@Composable
private fun FavoriteAddDialog(
    uiState: FavoriteCreateDialogUiState,
    lazyPagingItems: LazyPagingItems<Favorite>,
    onDismissRequest: () -> Unit,
    onFavoriteClick: (Favorite) -> Unit,
    onAddClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.favorite_add_title)) },
        text = {
            Column {
                HorizontalDivider(Modifier.alpha(if (!lazyListState.canScrollBackward) 0f else 1f))
                FavoriteAddContent(
                    lazyListState = lazyListState,
                    innerPadding = PaddingValues(),
                    lazyPagingItems = lazyPagingItems,
                    onFavoriteClick = onFavoriteClick
                ) {
                    item {
                        if (uiState.isShown) {
                            InputListItem(
                                value = uiState.name,
                                onValueChange = onNameChange,
                                isError = uiState.nameError,
                                onSaveClick = onSaveClick
                            )
                        } else {
                            NewFavoriteListItem(onAddClick)
                        }
                        HorizontalDivider()
                    }
                }
                HorizontalDivider(Modifier.alpha(if (!lazyListState.canScrollForward) 0f else 1f))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(R.string.favorite_add_action_close))
            }
        },
        modifier = Modifier.padding(vertical = ComicTheme.dimension.margin)
    )
}

@Composable
private fun NewFavoriteListItem(onClick: () -> Unit) {
    ListItem(
        modifier = Modifier
            .height(72.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            ),
        headlineContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(FavoriteCommonR.string.favorite_common_title_create))
                Icon(
                    imageVector = ComicIcons.Add,
                    contentDescription = null
                )
            }
        }
    )
}


@Composable
private fun InputListItem(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    onSaveClick: () -> Unit,
) {
    ListItem(
        headlineContent = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(imageVector = ComicIcons.Close, contentDescription = null)
                    }
                },
                label = {
                    Text(text = stringResource(id = FavoriteCommonR.string.favorite_common_label_favorite_name))
                },
                singleLine = true,
                isError = isError,
                supportingText = if (isError) {
                    {
                        Text(text = stringResource(id = FavoriteCommonR.string.favorite_common_message_error))
                    }
                } else {
                    null
                }
            )
        },
        trailingContent = {
            IconButton(onClick = onSaveClick) {
                Icon(
                    imageVector = ComicIcons.Save,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
private fun FavoriteAddContent(
    lazyListState: LazyListState,
    innerPadding: PaddingValues,
    lazyPagingItems: LazyPagingItems<Favorite>,
    onFavoriteClick: (Favorite) -> Unit,
    lazyContent: (LazyListScope.() -> Unit)? = null,
) {
    LazyColumn(
        state = lazyListState,
        contentPadding = innerPadding,
    ) {
        if (lazyContent != null) {
            lazyContent(this)
        }
        items(
            count = lazyPagingItems.itemCount,
            key = lazyPagingItems.itemKey { it.id.value }
        ) {
            val item = lazyPagingItems[it]
            if (item != null) {
                FavoriteItem(
                    favorite = item,
                    onClick = { onFavoriteClick(item) }
                )
            }
        }
    }
}
