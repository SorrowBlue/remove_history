package com.sorrowblue.comicviewer.library

import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.sorrowblue.comicviewer.bookshelf.BookshelfFragmentArgs
import com.sorrowblue.comicviewer.framework.ui.recyclerview.ViewBindingViewHolder
import com.sorrowblue.comicviewer.library.databinding.LibraryItemBinding

internal class LibraryAdapter : PagingDataAdapter<LibraryItem, LibraryAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<LibraryItem>() {
        override fun areItemsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
            return oldItem.library.id == newItem.library.id
        }

        override fun areContentsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class ViewHolder(parent: ViewGroup) :
        ViewBindingViewHolder<LibraryItemBinding>(parent, LibraryItemBinding::inflate) {
        fun bind(item: LibraryItem) {
            binding.textView.text = item.name
            binding.cover0.load(item.preview.getOrNull(0))
            binding.cover1.load(item.preview.getOrNull(1))
            binding.cover2.load(item.preview.getOrNull(2))
            binding.cover3.load(item.preview.getOrNull(3))
            binding.cover4.load(item.preview.getOrNull(4))
            binding.cover5.load(item.preview.getOrNull(5))
            binding.cover6.load(item.preview.getOrNull(6))
            binding.cover7.load(item.preview.getOrNull(7))
            binding.cover8.load(item.preview.getOrNull(8))
            binding.cover9.load(item.preview.getOrNull(9))
            binding.cover10.load(item.preview.getOrNull(10))
            binding.root.setOnClickListener {
                it.findNavController().navigate(
                    LibraryFragmentDirections.actionLibraryToBookshelfNavigation().actionId,
                    BookshelfFragmentArgs(item.library, null).toBundle()
                )
            }
        }
    }
}
