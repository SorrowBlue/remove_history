package com.sorrowblue.comicviewer.framework.ui.material3

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons

interface OverflowMenuScope {

    val state: OverflowMenuState
}

@Composable
fun OverflowMenuScope.OverflowMenuItem(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = { androidx.compose.material3.Text(text = text) },
        leadingIcon = { Icon(imageVector = icon, text) },
        onClick = {
            state.collapse()
            onClick()
        }
    )
}

@Composable
fun OverflowMenu(
    state: OverflowMenuState = rememberOverflowMenuState(),
    content: @Composable OverflowMenuScope.() -> Unit,
) {
    IconButton(onClick = state::expand) {
        Icon(ComicIcons.MoreVert, "Open Options")
    }
    DropdownMenu(
        expanded = state.expanded,
        onDismissRequest = state::collapse
    ) {
        content.invoke(object : OverflowMenuScope {
            override val state = state
        })
    }
}

class OverflowMenuState {
    var expanded by mutableStateOf(false)
        private set

    fun expand() {
        expanded = true
    }

    fun collapse() {
        expanded = false
    }
}

@Composable
fun rememberOverflowMenuState() = remember {
    OverflowMenuState()
}
