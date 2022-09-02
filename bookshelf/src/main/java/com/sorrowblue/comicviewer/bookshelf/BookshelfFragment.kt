package com.sorrowblue.comicviewer.bookshelf

import android.os.Bundle
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sorrowblue.comicviewer.bookshelf.databinding.BookshelfFragmentBinding
import com.sorrowblue.comicviewer.domain.model.Display
import com.sorrowblue.comicviewer.framework.ui.fragment.FrameworkFragment
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class BookshelfFragment : FrameworkFragment(R.layout.bookshelf_fragment) {

    private val binding: BookshelfFragmentBinding by viewBinding()

    private val viewModel: BookshelfViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = BookshelfAdapter(viewModel.library)
        binding.recyclerView.adapter = adapter
        fun updateAdapter(display: Display = viewModel.settings2.display) {
            adapter.display = display
            binding.recyclerView.layoutManager = when (display) {
                Display.GRID -> GridLayoutManager(
                    requireContext(),
                    resources.getInteger(R.integer.bookshelf_span)
                )
                Display.LIST -> LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
        }
        updateAdapter()
        binding.swipeRefreshLayout.setOnRefreshListener(adapter::refresh)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.data.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest(adapter::submitData)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.settings.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                updateAdapter(it.display)
                adapter.refresh()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .map { it.refresh is LoadState.Loading }
                .flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                binding.swipeRefreshLayout.setRefreshingForBinding(it)
            }
        }
        toolbar.title = viewModel.title
        toolbar.subtitle = viewModel.subTitle
    }
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.setRefreshingForBinding(isRefreshing: Boolean) {
    this.isRefreshing = isRefreshing
}
