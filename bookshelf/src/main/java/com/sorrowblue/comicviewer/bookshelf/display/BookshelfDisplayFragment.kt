package com.sorrowblue.comicviewer.bookshelf.display

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sorrowblue.comicviewer.bookshelf.R
import com.sorrowblue.comicviewer.bookshelf.databinding.BookshelfFragmentSortBinding
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import com.sorrowblue.comicviewer.domain.model.Display
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
internal class BookshelfDisplayFragment :
    BottomSheetDialogFragment(R.layout.bookshelf_fragment_sort) {

    private val binding: BookshelfFragmentSortBinding by viewBinding()
    private val viewModel: BookshelfDisplayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setting.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                binding.viewTypeGroup.check(
                    when (it.display) {
                        Display.GRID -> R.id.view_type_grid
                        Display.LIST -> R.id.view_type_list
                    }
                )
                binding.sortTypeGroup.check(
                    when (it.sort) {
                        BookshelfSettings.Sort.NAME -> R.id.sort_type_name
                        BookshelfSettings.Sort.DATE -> R.id.sort_type_date
                        BookshelfSettings.Sort.SIZE -> R.id.sort_type_size
                    }
                )
                binding.orderTypeGroup.check(
                    when (it.order) {
                        BookshelfSettings.Order.ASC -> R.id.order_type_asc
                        BookshelfSettings.Order.DESC -> R.id.order_type_desc
                    }
                )
            }
        }
        binding.viewTypeGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                viewModel.updateDisplay(
                    when (checkedId) {
                        R.id.view_type_grid -> Display.GRID
                        R.id.view_type_list -> Display.LIST
                        else -> TODO()
                    }
                )
            }
        }
        binding.sortTypeGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            logcat { "sortTypeGroup: checkedId=$checkedId, isChecked=$isChecked" }
            if (isChecked) {
                viewModel.updateSort(
                    when (checkedId) {
                        R.id.sort_type_name -> BookshelfSettings.Sort.NAME
                        R.id.sort_type_date -> BookshelfSettings.Sort.DATE
                        R.id.sort_type_size -> BookshelfSettings.Sort.SIZE
                        else -> TODO()
                    }
                )
            }
        }
        binding.orderTypeGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            logcat { "orderTypeGroup: checkedId=$checkedId, isChecked=$isChecked" }
            if (isChecked) {
                viewModel.updateOrder(
                    when (checkedId) {
                        R.id.order_type_asc -> BookshelfSettings.Order.ASC
                        R.id.order_type_desc -> BookshelfSettings.Order.DESC
                        else -> TODO()
                    }
                )
            }
        }
    }
}

fun Fragment.setBackStackEntryResult(key: String, value: Any) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}
