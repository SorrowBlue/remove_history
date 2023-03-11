package com.sorrowblue.comicviewer.file.list

import com.sorrowblue.comicviewer.framework.resource.R as FrameworkResourceR
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.viewbinding.ViewBinding
import coil.dispose
import coil.load
import com.sorrowblue.comicviewer.domain.entity.file.Book
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.entity.file.Folder
import com.sorrowblue.comicviewer.file.databinding.FileItemGridBinding
import com.sorrowblue.comicviewer.file.databinding.FileItemListBinding
import com.sorrowblue.comicviewer.framework.ui.recyclerview.ViewBindingViewHolder

sealed class FileViewHolder<V : ViewBinding>(
    parent: ViewGroup,
    inflate: (LayoutInflater, ViewGroup, Boolean) -> V,
    val onClick: (File, String, FragmentNavigator.Extras) -> Unit,
    val onLongClick: (File) -> Unit
) : ViewBindingViewHolder<V>(parent, inflate) {

    abstract fun bind(file: File?)
    abstract fun clear()

    internal class Grid(
        parent: ViewGroup,
        val isEnabledThumbnail: Boolean,
        onClick: (File, String, FragmentNavigator.Extras) -> Unit,
        onLongClick: (File) -> Unit
    ) : FileViewHolder<FileItemGridBinding>(
        parent,
        FileItemGridBinding::inflate,
        onClick,
        onLongClick
    ) {

        override fun clear() {
            binding.bookThumbnail.dispose()
            binding.folderThumbnail.dispose()
        }

        override fun bind(file: File?) {
            binding.root.transitionName = file?.path
            binding.file = file
            if (isEnabledThumbnail) {
                if (file is Book) {
                    binding.bookThumbnail.load(file) {
                        error(FrameworkResourceR.drawable.ic_twotone_broken_image_24)
                    }
                    binding.folderThumbnail.setImageDrawable(null)
                } else if (file is Folder) {
                    binding.bookThumbnail.setImageDrawable(null)
                    binding.folderThumbnail.load(file) {
                        error(FrameworkResourceR.drawable.ic_twotone_broken_image_24)
                    }
                }
            } else {
                if (file is Book) {
                    binding.bookThumbnail.setImageResource(FrameworkResourceR.drawable.ic_twotone_book_24)
                    binding.folderThumbnail.setImageDrawable(null)
                } else if (file is Folder) {
                    binding.bookThumbnail.setImageDrawable(null)
                    binding.folderThumbnail.setImageDrawable(null)
                }
            }
            binding.root.setOnLongClickListener {
                if (file != null) {
                    onLongClick(file)
                    true
                } else {
                    false
                }
            }
            binding.root.setOnClickListener {
                if (file != null) {
                    onClick.invoke(
                        file,
                        binding.root.transitionName,
                        FragmentNavigatorExtras(binding.root to binding.root.transitionName)
                    )
                }
            }
        }
    }

    internal class List(
        parent: ViewGroup,
        val isEnabledThumbnail: Boolean,
        onClick: (File, String, FragmentNavigator.Extras) -> Unit,
        onLongClick: (File) -> Unit
    ) : FileViewHolder<FileItemListBinding>(
        parent,
        FileItemListBinding::inflate,
        onClick,
        onLongClick
    ) {

        override fun clear() {
            binding.folderThumbnail.dispose()
            binding.bookThumbnail.dispose()
        }

        override fun bind(file: File?) {
            binding.file = file
            if (isEnabledThumbnail) {
                if (file is Book) {
                    binding.bookThumbnail.load(file) {
                        error(FrameworkResourceR.drawable.ic_twotone_broken_image_24)
                    }
                    binding.folderThumbnail.setImageDrawable(null)
                } else if (file is Folder) {
                    binding.bookThumbnail.setImageDrawable(null)
                    binding.folderThumbnail.load(file) {
                        error(FrameworkResourceR.drawable.ic_twotone_broken_image_24)
                    }
                }
            } else {
                if (file is Book) {
                    binding.bookThumbnail.setImageResource(FrameworkResourceR.drawable.ic_twotone_book_24)
                    binding.folderThumbnail.setImageDrawable(null)
                } else if (file is Folder) {
                    binding.bookThumbnail.setImageDrawable(null)
                    binding.folderThumbnail.setImageDrawable(null)
                }
            }
            binding.root.setOnLongClickListener {
                if (file != null) {
                    onLongClick(file)
                    true
                } else {
                    false
                }
            }
            binding.root.setOnClickListener {
                if (file != null) {
                    onClick.invoke(
                        file,
                        binding.root.transitionName,
                        FragmentNavigatorExtras(binding.root to binding.root.transitionName)
                    )
                }
            }
        }
    }
}
