package com.sorrowblue.comicviewer.library.googledrive.section

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sorrowblue.comicviewer.framework.compose.AppMaterialTheme
import com.sorrowblue.comicviewer.framework.compose.placeholder.debugPlaceholder

internal sealed interface GoogleAccountDialogUiState {

    data object Hide : GoogleAccountDialogUiState
    data class Show(
        val photoUrl: String = "",
        val name: String = ""
    ) : GoogleAccountDialogUiState
}

@Composable
internal fun GoogleAccountDialog(
    uiState: GoogleAccountDialogUiState,
    onDismissRequest: () -> Unit,
    onLogoutClick: () -> Unit
) {
    when (uiState) {
        GoogleAccountDialogUiState.Hide -> Unit
        is GoogleAccountDialogUiState.Show ->
            GoogleAccountDialog(
                uiState = uiState,
                onDismissRequest = onDismissRequest,
                onLogoutClick = onLogoutClick,
            )
    }
}

@Composable
private fun GoogleAccountDialog(
    uiState: GoogleAccountDialogUiState.Show = GoogleAccountDialogUiState.Show(),
    onDismissRequest: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onLogoutClick) {
                Icon(imageVector = Icons.TwoTone.Logout, contentDescription = null)
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Logout")
            }
        },
        icon = {
            AsyncImage(
                model = uiState.photoUrl,
                contentDescription = null,
                placeholder = debugPlaceholder(),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
        },
        title = {
            Text(
                text = uiState.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    )
}

@Preview
@Composable
private fun PreviewGoogleAccountDialog() {
    AppMaterialTheme {
        Surface {
            GoogleAccountDialog()
        }
    }
}
