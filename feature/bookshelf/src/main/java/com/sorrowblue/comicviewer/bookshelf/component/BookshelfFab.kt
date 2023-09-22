package com.sorrowblue.comicviewer.bookshelf.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sorrowblue.comicviewer.feature.bookshelf.R

@Composable
fun BookshelfFab(expanded: Boolean, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        text = { Text(stringResource(R.string.bookshelf_label_new_bookshelf)) },
        icon = {
            Icon(
                Icons.TwoTone.Add,
                contentDescription = stringResource(R.string.bookshelf_label_new_bookshelf)
            )
        },
        expanded = expanded
    )
}
