package com.sorrowblue.comicviewer.viewer

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearSnapHelper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sorrowblue.comicviewer.framework.ui.fragment.FrameworkFragment
import com.sorrowblue.comicviewer.viewer.databinding.ViewerFragmentBinding
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
internal class ViewerFragment : FrameworkFragment(R.layout.viewer_fragment) {

    private val binding: ViewerFragmentBinding by viewBinding()

    private val viewModel: ViewerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        commonViewModel.isVisibleToolbar.value = false
        val adapter = ComicFragmentStateAdapter(this, viewModel.book)
//        val adapter = ViewerTestAdapter {
//            viewModel.isVisibleUI.value = !viewModel.isVisibleUI.value
//        }
//        LinearSnapHelper().attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.offscreenPageLimit = 2
        binding.slider.setLabelFormatter { it.toInt().plus(1).toString() }
        binding.slider.valueTo = viewModel.max.minus(1).toFloat()
        val standardBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isVisibleUI
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest {
                    if (it) {
                        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        binding.viewerAppBarLayout.setExpanded(true)
                    } else {
                        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        binding.viewerAppBarLayout.setExpanded(false)
                    }
                }
        }
        standardBottomSheetBehavior.isHideable = true
        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        binding.root.setOnKeyListener { _, i, keyEvent ->
            logcat { "i=$i, action=${keyEvent.action}" }
            if (i == KeyEvent.KEYCODE_VOLUME_DOWN && keyEvent.action == KeyEvent.ACTION_DOWN) {
                viewModel.next()
                true
            } else if (i == KeyEvent.KEYCODE_VOLUME_UP && keyEvent.action == KeyEvent.ACTION_DOWN) {
                viewModel.back()
                true
            } else {
                false
            }
        }
        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
    }
}
