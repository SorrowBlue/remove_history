package com.sorrowblue.comicviewer.library.filelist

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.sorrowblue.comicviewer.domain.entity.file.Book
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.entity.file.Folder
import com.sorrowblue.comicviewer.framework.ui.recyclerview.ViewBindingViewHolder
import com.sorrowblue.comicviewer.library.databinding.LibraryItemFileListBinding

abstract class LibraryFileListAdapter(
    private val download: (Book) -> Unit,
    private val folder: (Folder) -> Unit
) : PagingDataAdapter<File, LibraryFileListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File) =
            oldItem.bookshelfId == newItem.bookshelfId && oldItem.path == newItem.path

        override fun areContentsTheSame(oldItem: File, newItem: File) = oldItem == newItem
    }
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    abstract inner class ViewHolder(parent: ViewGroup) :
        ViewBindingViewHolder<LibraryItemFileListBinding>(
            parent,
            LibraryItemFileListBinding::inflate
        ) {

        @CallSuper
        open fun bind(file: File) {
            binding.file = file
            binding.root.setOnClickListener {
                when (file) {
                    is Book -> download(file)
                    is Folder -> folder(file)
                }
            }
        }
    }
}
