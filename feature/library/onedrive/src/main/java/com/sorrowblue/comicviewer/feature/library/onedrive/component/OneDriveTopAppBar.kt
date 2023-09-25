package com.sorrowblue.comicviewer.feature.library.onedrive.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sorrowblue.comicviewer.framework.designsystem.icon.ComicIcons
import com.sorrowblue.comicviewer.framework.ui.debugPlaceholder
import java.io.InputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
internal fun OneDriveTopAppBar(
    path: String,
    profileUri: suspend () -> InputStream?,
    onBackClick: () -> Unit,
    onProfileImageClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior?,
) {
    var inputStream by remember { mutableStateOf<ByteArray?>(null) }
    LaunchedEffect(profileUri) {
        launch(Dispatchers.IO) {
            inputStream = profileUri()?.readBytes()
        }
    }
    TopAppBar(
        title = {
            Column {
                Text(text = "OneDrive")
                if (path.isNotEmpty()) {
                    Text(
                        text = path,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = ComicIcons.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            AsyncImage(
                model = inputStream,
                placeholder = debugPlaceholder(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(9.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onProfileImageClick)
            )
        },
        scrollBehavior = scrollBehavior
    )
}
