package com.sorrowblue.comicviewer.library.box.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sorrowblue.comicviewer.library.box.data.BoxApiRepository

internal class BoxApiViewModel(application: Application) : AndroidViewModel(application) {

    val repository = BoxApiRepository.getInstance(application)
}
