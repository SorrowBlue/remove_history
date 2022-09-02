package com.sorrowblue.comicviewer.management.edit

import android.os.Bundle
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.databinding.InverseMethod
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sorrowblue.comicviewer.framework.ui.fragment.FrameworkFragment
import com.sorrowblue.comicviewer.management.R
import com.sorrowblue.comicviewer.management.databinding.ManagementFragmentEditSmbBinding
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat


@AndroidEntryPoint
internal class ManagementEditSmbFragment :
    FrameworkFragment(R.layout.management_fragment_edit_smb) {

    private val binding: ManagementFragmentEditSmbBinding by viewBinding()
    private val viewModel: ManagementEditSmbViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isConnecting.collectLatest {
                binding.fab.isVisible = !it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isError.collectLatest {
                binding.fab.isEnabled = !it
            }
        }
        binding.fab.setImageResource(com.sorrowblue.comicviewer.framework.R.drawable.ic_twotone_save_24)
        binding.fab.setOnClickListener {
            binding.host.editText?.setText(viewModel.host.value)
            if (viewModel.isGuest.value) {
                binding.host.editText?.setText(viewModel.host.value)
            } else {
                binding.host.editText?.setText(viewModel.host.value)
                binding.username.editText?.setText(viewModel.username.value)
                binding.password.editText?.setText(viewModel.password.value)
            }
            WindowInsetsControllerCompat(requireActivity().window, binding.root)
                .hide(WindowInsetsCompat.Type.ime())
            viewModel.connect {
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest {
                logcat { "viewModel.result = $it" }
                findNavController().popBackStack()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.message.collectLatest {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG)
                        .setAnchorView(binding.fab)
                        .show()
                }
            }
        }
    }
}


object Converter {

    @JvmStatic
    @InverseMethod("buttonIdToBoolean")
    fun booleanToButtonId(value: Boolean): Int {
        return if (value) R.id.guest else R.id.username_password
    }

    @JvmStatic
    fun buttonIdToBoolean(value: Int): Boolean {
        return value == R.id.guest
    }
}

