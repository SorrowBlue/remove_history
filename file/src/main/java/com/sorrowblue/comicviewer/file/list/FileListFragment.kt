package com.sorrowblue.comicviewer.file.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import androidx.core.view.updatePadding
import androidx.navigation.fragment.FragmentNavigator
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sorrowblue.comicviewer.domain.entity.file.File
import com.sorrowblue.comicviewer.domain.entity.settings.FolderDisplaySettings
import com.sorrowblue.comicviewer.file.R
import com.sorrowblue.comicviewer.file.info.FileInfoNavigation
import com.sorrowblue.comicviewer.framework.ui.flow.launchInWithLifecycle
import com.sorrowblue.comicviewer.framework.ui.fragment.PagingFragment
import com.sorrowblue.comicviewer.framework.ui.fragment.type
import com.sorrowblue.comicviewer.framework.ui.widget.ktx.setSpanCount
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.InsetterDsl
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import logcat.logcat

@AndroidEntryPoint
abstract class FileListFragment(contentLayoutId: Int) : PagingFragment<File>(contentLayoutId) {

    private val toolbar: Toolbar get() = requireView().requireViewById(com.sorrowblue.comicviewer.framework.ui.R.id.toolbar)
    private val recyclerView: RecyclerView get() = requireView().requireViewById(com.sorrowblue.comicviewer.framework.ui.R.id.recycler_view)

    abstract override val viewModel: FileListViewModel

    abstract fun navigateToFile(
        file: File,
        transitionName: String,
        extras: FragmentNavigator.Extras
    )

    override val adapter
        get() = FileListAdapter(
            runBlocking { viewModel.displayFlow.first() },
            runBlocking { viewModel.isEnabledThumbnailFlow.first() },
            { file, transitionName, extras -> navigateToFile(file, transitionName, extras) },
            { navigate(FileInfoNavigation.getDeeplink(it)) }
        )

    override fun onCreateAdapter(pagingDataAdapter: PagingDataAdapter<File, *>) {
        super.onCreateAdapter(pagingDataAdapter)
        check(pagingDataAdapter is FileListAdapter)

        combine(viewModel.displayFlow, viewModel.orderFlow, viewModel.sortFlow) { display, _, _ ->
            pagingDataAdapter.setDisplay(display)
        }.launchInWithLifecycle()

        var itemDecoration: AdaptiveSpacingItemDecoration? = null
        viewModel.displayFlow.combine(viewModel.columnSizeFlow) { display, columnSize ->
            when (display) {
                FolderDisplaySettings.Display.LIST -> {
                    itemDecoration?.let(recyclerView::removeItemDecoration)
                    defaultMargin = 0
                    recyclerView.requestApplyInsets()
                    recyclerView.setSpanCount(1)
                }

                FolderDisplaySettings.Display.GRID -> {
                    defaultMargin =
                        resources.getDimensionPixelSize(com.sorrowblue.comicviewer.framework.ui.R.dimen.framework_ui_default_margin)
                    recyclerView.requestApplyInsets()
                    val spanCount = when (columnSize) {
                        FolderDisplaySettings.Size.SMALL -> R.integer.file_list_span_count_small
                        FolderDisplaySettings.Size.MEDIUM -> R.integer.file_list_span_count_medium
                        FolderDisplaySettings.Size.LARGE -> R.integer.file_list_span_count_large
                    }.let {
                        resources.getInteger(it)
                    }
                    recyclerView.setSpanCount(spanCount)
                    val itemSpace = when (columnSize) {
                        FolderDisplaySettings.Size.SMALL -> R.dimen.file_list_item_space_small
                        FolderDisplaySettings.Size.MEDIUM -> R.dimen.file_list_item_space_medium
                        FolderDisplaySettings.Size.LARGE -> R.dimen.file_list_item_space_large
                    }.let(resources::getDimensionPixelSize)
                    itemDecoration?.let {
                        it.size = itemSpace
                        if (recyclerView.itemDecorationCount == 0) {
                            recyclerView.addItemDecoration(it)
                        } else {
                            recyclerView.invalidateItemDecorations()
                        }
                    } ?: recyclerView.addItemDecoration(AdaptiveSpacingItemDecoration(itemSpace).also {
                        itemDecoration = it
                    })
                    recyclerView.requestApplyInsets()
                }
            }
        }.launchInWithLifecycle()
    }

    var defaultMargin = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setupWithNavController()
        ViewCompat.setOnApplyWindowInsetsListener(recyclerView) { v, insets ->
            val i = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            v.updatePadding(left = i.left + defaultMargin, right = i.right + defaultMargin, bottom = i.bottom + defaultMargin)
            WindowInsetsCompat.CONSUMED
        }
        toolbar.applyInsetter {
            type(systemBars = true, displayCutout = true) {
                padding(horizontal = true)
                margin(top = true)
            }
        }
    }
}
