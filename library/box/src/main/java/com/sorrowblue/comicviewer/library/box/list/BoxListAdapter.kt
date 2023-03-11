package com.sorrowblue.comicviewer.library.box.list

import android.view.ViewGroup
import coil.load
import com.sorrowblue.comicviewer.domain.entity.file.Book
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.entity.file.Folder
import com.sorrowblue.comicviewer.library.filelist.LibraryFileListAdapter

internal class BoxListAdapter(download: (Book) -> Unit, folder: (Folder) -> Unit) :
    LibraryFileListAdapter(download, folder) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    inner class ViewHolder(parent: ViewGroup) : LibraryFileListAdapter.ViewHolder(parent) {
        override fun bind(file: File) {
            if (file is Folder) {
                binding.icon.setImageResource(com.sorrowblue.comicviewer.framework.resource.R.drawable.ic_twotone_folder_open_24)
            } else {
                binding.icon.load(file.params["thumbnail"]) {
                    addHeader("Authorization", "Bearer ${file.params["access_token"]}")
                }
            }
        }
    }
}
