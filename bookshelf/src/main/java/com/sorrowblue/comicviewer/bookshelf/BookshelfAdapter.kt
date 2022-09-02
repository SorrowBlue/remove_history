package com.sorrowblue.comicviewer.bookshelf

import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.sorrowblue.comicviewer.bookshelf.databinding.BookshelfItemListBinding
import com.sorrowblue.comicviewer.domain.model.Display
import com.sorrowblue.comicviewer.domain.model.file.Book
import com.sorrowblue.comicviewer.domain.model.file.Bookshelf
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.domain.model.library.Library
import com.sorrowblue.comicviewer.framework.ui.recyclerview.ViewBindingViewHolder
import com.sorrowblue.comicviewer.viewer.ViewerFragmentArgs

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<File>() {
    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.path == newItem.path
    }

    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
    }
}

internal class BookshelfAdapter(
    private val library: Library,
) :
    PagingDataAdapter<File, BookshelfAdapter.ViewHolder>(DIFF_CALLBACK) {


    lateinit var display: Display

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(parent: ViewGroup) :
        ViewBindingViewHolder<BookshelfItemListBinding>(parent, BookshelfItemListBinding::inflate) {

        fun bind(item: File?) {
            when (display) {
                Display.GRID -> {
                    binding.root.orientation = LinearLayout.VERTICAL
                    binding.name.gravity = Gravity.CENTER_HORIZONTAL
                    binding.name.updateLayoutParams<LinearLayout.LayoutParams> {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                }
                Display.LIST -> {
                    binding.root.orientation = LinearLayout.HORIZONTAL
                    binding.name.gravity = Gravity.CENTER_VERTICAL
                    binding.name.updateLayoutParams<LinearLayout.LayoutParams> {
                        gravity = Gravity.CENTER_VERTICAL
                    }
                }
            }
            binding.name.text = item?.name.orEmpty()
            when (item) {
                is Book -> {
                    binding.loading.isVisible = item.preview.isEmpty()
                    binding.cover.load(item.preview)
                    binding.folder.isVisible = false
                    binding.cover.isVisible = true
                }
                is Bookshelf -> {
                    binding.loading.isVisible = false
                    binding.cover.isVisible = false
                    binding.folder.isVisible = true
                }
                null -> {
                    binding.name.text = null
                    binding.folder.isVisible = false
                    binding.loading.isVisible = true
                }
            }
            binding.root.setOnClickListener {
                when (item) {
                    is Book ->
                        it.findNavController()
                            .navigate(
                                BookshelfFragmentDirections.actionToViewer().actionId,
                                ViewerFragmentArgs(library, item).toBundle()
                            )
                    is Bookshelf -> it.findNavController()
                        .navigate(BookshelfFragmentDirections.actionBookshelfFragmentSelf(library,
                            item))
                    null -> {


                    }
                }
            }
        }
    }
}
