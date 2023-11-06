package com.sorrowblue.comicviewer.feature.tutorial.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sorrowblue.comicviewer.feature.tutorial.SplitInstallError
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons
import com.sorrowblue.comicviewer.framework.designsystem.icon.undraw.UndrawResumeFolder
import com.sorrowblue.comicviewer.framework.designsystem.theme.ComicTheme
import com.sorrowblue.comicviewer.framework.ui.PreviewComic

internal sealed interface DocumentSheetUiState {

    val isButtonEnabled: Boolean
    val buttonText: String

    val isProgressVisible: Boolean
    val progress: Float

    data object Pending : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "PENDING"
        override val isProgressVisible = true
        override val progress = 0f
    }

    data object RequestsUserConfirmation : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "REQUIRES_USER_CONFIRMATION"
        override val isProgressVisible = true
        override val progress = 0f
    }

    data class DOWNLOADING(override val progress: Float) : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "DOWNLOADING"
        override val isProgressVisible = true
    }

    data object DOWNLOADED : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "DOWNLOADED"
        override val isProgressVisible = true
        override val progress = 0f
    }

    data object INSTALLING : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "INSTALLING"
        override val isProgressVisible = true
        override val progress = 0f
    }

    data object INSTALLED : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "INSTALLED"
        override val isProgressVisible = false
        override val progress = 0f
    }

    data class FAILED(val splitInstallError: SplitInstallError) : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "INSTALLED"
        override val isProgressVisible = false
        override val progress = 0f
    }

    data object CANCELING : DocumentSheetUiState {
        override val isButtonEnabled = false
        override val buttonText = "CANCELING"
        override val isProgressVisible = true
        override val progress = 0f
    }

    data object CANCELED : DocumentSheetUiState {
        override val isButtonEnabled = true
        override val buttonText = "DOWNLOAD"
        override val isProgressVisible = false
        override val progress = 0f
    }

    data object NONE : DocumentSheetUiState {
        override val isButtonEnabled = true
        override val buttonText = "DOWNLOAD"
        override val isProgressVisible = false
        override val progress = 0f
    }
}

@Composable
internal fun DocumentSheet(
    uiState: DocumentSheetUiState,
    onDownloadClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(ComicTheme.dimension.margin),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            ComicIcons.UndrawResumeFolder,
            contentDescription = null,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth(0.5f),
        )
        Text(
            text = "Document Support",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec sit amet finibus elit. Vivamus scelerisque, risus eu accumsan interdum, nunc quam aliquet lectus, vel aliquam ipsum lectus eu urna. Morbi quis elementum turpis, sed ultrices sem. Nullam vel viverra libero. Cras in porttitor erat. Nunc eget velit a justo sagittis blandit lobortis a mi.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.size(16.dp))

        if (uiState.isProgressVisible) {
            LinearProgressIndicator(
                progress = uiState::progress,
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        TextButton(onClick = onDownloadClick, enabled = uiState.isButtonEnabled) {
            Row {
                Icon(ComicIcons.InstallMobile, contentDescription = null)
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

                Text(text = uiState.buttonText)
            }
        }
    }
}

@PreviewComic
@Composable
private fun PreviewDocumentSheet() {
    ComicTheme {
        Surface {
            DocumentSheet(uiState = DocumentSheetUiState.INSTALLED, onDownloadClick = {})
        }
    }
}
