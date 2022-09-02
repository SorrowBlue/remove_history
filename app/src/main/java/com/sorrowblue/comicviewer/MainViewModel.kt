package com.sorrowblue.comicviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sorrowblue.comicviewer.domain.model.file.Bookshelf
import com.sorrowblue.comicviewer.domain.model.library.Library
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
internal class MainViewModel @Inject constructor(
) : ViewModel() {

    var shouldKeepOnScreen = true

    val list =
        MutableSharedFlow<Pair<Library?, List<Bookshelf>>>(0, 1, BufferOverflow.DROP_OLDEST)

    init {
        viewModelScope.launch {
            delay(1000)
//            val (bookshelfInfo, historyComicInfo) = withContext(Dispatchers.IO) {
//                val history = historyRepository.history.first()
//                bookshelfInfoRepository.find(history.bookshelfId) to historyRepository.history.first().currentComic?.let {
//                    comicInfoRepository.find(it)
//                }
//            }
            list.emit(null to emptyList())
            return@launch
//            if (bookshelfInfo == null) {
//                list.emit(null to emptyList())
//            } else if (historyComicInfo == null) {
//                list.emit(bookshelfInfo to emptyList())
//            } else {
//                val basePath = Uri.Builder()
//                    .scheme("smb")
//                    .authority(bookshelfInfo.info.host)
//                    .path(bookshelfInfo.info.path)
//                    .build().toString()
//                val pathList =
//                    historyComicInfo.path.removePrefix(basePath).removeSuffix("/").split("/")
//                if (pathList.isEmpty()) {
//                    list.emit(bookshelfInfo to emptyList())
//                } else {
//                    var tmp = basePath
//                    val paths = mutableListOf<ComicInfo>()
//                    pathList.forEach {
//                        tmp = "$tmp$it/"
//                        comicInfoRepository.find(tmp)?.let(paths::add)
//                            ?: return@forEach
//                    }
//                    list.emit(bookshelfInfo to paths)
//                }
//            }
        }
    }
}
