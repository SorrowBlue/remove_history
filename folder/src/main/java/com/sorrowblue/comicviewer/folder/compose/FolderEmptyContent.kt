package com.sorrowblue.comicviewer.folder.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.sorrowblue.comicviewer.folder.R
import com.sorrowblue.comicviewer.framework.resource.R as ResourceR

@Composable
fun FolderEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(ResourceR.drawable.ic_undraw_resume_folder_re_e0bi),
            ""
        )
        Text(stringResource(id = R.string.folder_label_no_file))
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewFolderEmptyContent() {
    FolderEmptyContent()
}
