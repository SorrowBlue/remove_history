package com.sorrowblue.comicviewer.readlater

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.paging.PagingDataAdapter
import com.sorrowblue.comicviewer.book.BookFragmentArgs
import com.sorrowblue.comicviewer.bookshelf.BookshelfAdapter
import com.sorrowblue.comicviewer.bookshelf.BookshelfFragmentArgs
import com.sorrowblue.comicviewer.domain.entity.file.Book
import com.sorrowblue.comicviewer.domain.entity.file.Folder
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.file.info.FileInfoFragmentArgs
import com.sorrowblue.comicviewer.framework.ui.fragment.CommonViewModel
import com.sorrowblue.comicviewer.framework.ui.fragment.PagingFragment
import com.sorrowblue.comicviewer.framework.ui.fragment.encodeBase64
import com.sorrowblue.comicviewer.framework.ui.fragment.launchInWithLifecycle
import com.sorrowblue.comicviewer.framework.ui.fragment.type
import com.sorrowblue.comicviewer.framework.ui.widget.ktx.setSpanCount
import com.sorrowblue.comicviewer.readlater.databinding.ReadlaterFragmentBinding
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
internal class ReadLaterFragment : PagingFragment<File>(R.layout.readlater_fragment),
    Toolbar.OnMenuItemClickListener {

    private val binding: ReadlaterFragmentBinding by viewBinding()
    private val commonViewModel: CommonViewModel by activityViewModels()

    override val viewModel: ReadLaterViewModel by viewModels()
    override val recyclerView get() = binding.recyclerView
    override val adapter: PagingDataAdapter<File, *>
        get() = BookshelfAdapter(
            runBlocking { viewModel.folderDisplaySettingsFlow.first().display },
            { file, transitionName, extras ->
                when (file) {
                    is Book -> navigate(
                        ReadLaterFragmentDirections.actionReadlaterToBook(file, transitionName),
                        extras
                    )

                    is Folder -> navigate(
                        ReadLaterFragmentDirections.actionReadlaterToBookshelf(
                            file,
                            transitionName
                        ),
                        extras
                    )
                }
            },
            { navigate(ReadLaterFragmentDirections.actionReadlaterToFileInfo(it)) }
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonViewModel.isVisibleFab.value = true
        commonViewModel.isVisibleBottomNavigation.value = true

        binding.viewModel = viewModel

        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.setOnMenuItemClickListener(this)
        binding.toolbar.applyInsetter {
            type(systemBars = true, displayCutout = true) {
                padding(horizontal = true)
                margin(top = true)
            }
        }

        binding.recyclerView.applyInsetter {
            type(systemBars = true, displayCutout = true) {
                padding(horizontal = true, bottom = true)
            }
        }
    }

    override fun onCreateAdapter(adapter: PagingDataAdapter<File, *>) {
        super.onCreateAdapter(adapter)
        check(adapter is BookshelfAdapter)
        viewModel.folderDisplaySettingsFlow.onEach { adapter.display = it.display }
            .launchInWithLifecycle()
        viewModel.spanCountFlow.onEach(binding.recyclerView::setSpanCount).launchInWithLifecycle()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            else -> item.onNavDestinationSelected(findNavController())
        }
    }

    private fun ReadLaterFragmentDirections.Companion.actionReadlaterToBook(
        book: Book,
        transitionName: String
    ) = object : NavDirections {
        override val actionId = actionReadlaterToBook().actionId
        override val arguments = BookFragmentArgs(
            book.serverId.value,
            book.path.encodeBase64(),
            transitionName,
            book.lastPageRead
        ).toBundle()
    }

    private fun ReadLaterFragmentDirections.Companion.actionReadlaterToBookshelf(
        folder: Folder,
        transitionName: String
    ) = object : NavDirections {
        override val actionId = actionReadlaterToBookshelf().actionId
        override val arguments = BookshelfFragmentArgs(
            folder.serverId.value,
            folder.path.encodeBase64(),
            transitionName
        ).toBundle()
    }

    private fun ReadLaterFragmentDirections.Companion.actionReadlaterToFileInfo(file: File) =
        object : NavDirections {
            override val actionId = actionReadlaterToFileInfo().actionId
            override val arguments = FileInfoFragmentArgs(
                file.serverId.value,
                file.path.encodeBase64(),
            ).toBundle()
        }
}
