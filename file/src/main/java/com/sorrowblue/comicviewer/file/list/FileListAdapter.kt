package com.sorrowblue.comicviewer.file.list

import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.entity.settings.FolderDisplaySettings

class FileListAdapter(
    private var display: FolderDisplaySettings.Display,
    var isEnabledThumbnail: Boolean,
    private val onClick: (File, String, FragmentNavigator.Extras) -> Unit,
    private val onLongClick: (File) -> Unit
) : PagingDataAdapter<File, FileViewHolder<out ViewBinding>>(
    object : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File) = oldItem.path == newItem.path
        override fun areContentsTheSame(oldItem: File, newItem: File) =
            oldItem.path == newItem.path && oldItem.lastModifier == newItem.lastModifier && oldItem.size == newItem.size
    }
) {

    fun setDisplay(display: FolderDisplaySettings.Display) {
        this.display = display
        refresh()
    }

    override fun getItemViewType(position: Int) = display.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (display) {
        FolderDisplaySettings.Display.GRID -> FileViewHolder.Grid(parent, isEnabledThumbnail,onClick, onLongClick)
        FolderDisplaySettings.Display.LIST -> FileViewHolder.List(parent, isEnabledThumbnail,onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: FileViewHolder<out ViewBinding>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: FileViewHolder<out ViewBinding>) {
        super.onViewRecycled(holder)
        holder.clear()
    }
}
