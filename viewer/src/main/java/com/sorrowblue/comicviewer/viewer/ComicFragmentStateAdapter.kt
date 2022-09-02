package com.sorrowblue.comicviewer.viewer

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sorrowblue.comicviewer.domain.model.file.Book

internal class ComicFragmentStateAdapter(fragment: Fragment, private val book: Book) :
    FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    override fun getItemCount() = book.maxPage

    override fun createFragment(position: Int): Fragment {
        return ComicPageFragment().apply {
            arguments = ComicPageFragmentArgs(position).toBundle()
        }
    }
}
