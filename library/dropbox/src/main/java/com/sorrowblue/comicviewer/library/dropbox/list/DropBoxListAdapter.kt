package com.sorrowblue.comicviewer.library.dropbox.list

import android.view.ViewGroup
import coil.load
import com.sorrowblue.comicviewer.domain.entity.file.Book
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.entity.file.Folder
import com.sorrowblue.comicviewer.library.filelist.LibraryFileListAdapter

internal class DropBoxListAdapter(download: (Book) -> Unit, folder: (Folder) -> Unit) :
    LibraryFileListAdapter(download, folder) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) : LibraryFileListAdapter.ViewHolder(parent) {
        override fun bind(file: File) {
            super.bind(file)
            binding.icon.load(file.params["preview_url"])
        }
    }
}
