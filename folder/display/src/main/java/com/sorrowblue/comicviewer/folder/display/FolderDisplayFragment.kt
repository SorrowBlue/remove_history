package com.sorrowblue.comicviewer.folder.display

import android.app.Dialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.sidesheet.SideSheetDialog
import com.sorrowblue.comicviewer.domain.entity.settings.FolderDisplaySettings
import com.sorrowblue.comicviewer.folder.display.databinding.FolderDisplayFragmentBinding
import com.sorrowblue.comicviewer.framework.ui.fragment.launchInWithDialogLifecycle
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
internal class FolderDisplayFragment : DialogFragment() {

    private val binding: FolderDisplayFragmentBinding by viewBinding()
    private val viewModel: FolderDisplayViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return SideSheetDialog(requireContext()).apply {
            setContentView(binding.root)
            onDialogCreated()
        }
    }

    private fun onDialogCreated() {
        // View type
        binding.viewTypeGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.view_type_grid -> viewModel.update(FolderDisplaySettings.Display.GRID)
                R.id.view_type_list -> viewModel.update(FolderDisplaySettings.Display.LIST)
            }
        }
        viewModel.displayFlow.onEach {
            binding.viewTypeGroup.check(
                when (it) {
                    FolderDisplaySettings.Display.GRID -> R.id.view_type_grid
                    FolderDisplaySettings.Display.LIST -> R.id.view_type_list
                }
            )
            binding.spanCountLabel.isVisible = it == FolderDisplaySettings.Display.GRID
            binding.columSizeGroup.isVisible = it == FolderDisplaySettings.Display.GRID
        }.launchInWithDialogLifecycle()

        // ColumSize
        binding.columSizeGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.colum_size_small -> viewModel.update(FolderDisplaySettings.Size.SMALL)
                R.id.colum_size_medium -> viewModel.update(FolderDisplaySettings.Size.MEDIUM)
                R.id.colum_size_large -> viewModel.update(FolderDisplaySettings.Size.LARGE)
            }
        }
        viewModel.columnSizeFlow.onEach {
            binding.columSizeGroup.check(
                when (it) {
                    FolderDisplaySettings.Size.SMALL -> R.id.colum_size_small
                    FolderDisplaySettings.Size.MEDIUM -> R.id.colum_size_medium
                    FolderDisplaySettings.Size.LARGE -> R.id.colum_size_large
                }
            )
        }.launchInWithDialogLifecycle()

        // Sort
        binding.sortTypeGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.sort_type_date -> viewModel.update(FolderDisplaySettings.Sort.DATE)
                R.id.sort_type_name -> viewModel.update(FolderDisplaySettings.Sort.NAME)
                R.id.sort_type_size -> viewModel.update(FolderDisplaySettings.Sort.SIZE)
            }
        }
        viewModel.sortFlow.onEach {
            binding.sortTypeGroup.check(
                when (it) {
                    FolderDisplaySettings.Sort.DATE -> R.id.sort_type_date
                    FolderDisplaySettings.Sort.NAME -> R.id.sort_type_name
                    FolderDisplaySettings.Sort.SIZE -> R.id.sort_type_size
                }
            )
        }.launchInWithDialogLifecycle()

        // Order
        binding.orderTypeGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when (checkedIds.firstOrNull()) {
                R.id.order_type_asc -> viewModel.update(FolderDisplaySettings.Order.ASC)
                R.id.order_type_desc -> viewModel.update(FolderDisplaySettings.Order.DESC)
            }
        }
        viewModel.orderFlow.onEach {
            binding.orderTypeGroup.check(
                when (it) {
                    FolderDisplaySettings.Order.ASC -> R.id.order_type_asc
                    FolderDisplaySettings.Order.DESC -> R.id.order_type_desc
                }
            )
        }.launchInWithDialogLifecycle()
    }
}
