package com.sorrowblue.comicviewer.library

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.sorrowblue.comicviewer.framework.ui.fragment.FrameworkFragment
import com.sorrowblue.comicviewer.library.databinding.LibraryFragmentBinding
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
internal class LibraryFragment : FrameworkFragment(R.layout.library_fragment) {

    private val binding: LibraryFragmentBinding by viewBinding()
    private val viewModel: LibraryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LibraryAdapter()
        binding.recyclerView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.libraryInfo.mapLatest { pagingData -> pagingData.map(viewModel::bookshelfToLibraryItem) }
                .flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest(adapter::submitData)
        }
        binding.fab.setOnClickListener {
            request()
            findNavController().navigate(LibraryFragmentDirections.actionLibraryToManagementProtocolNavigation())
        }
        val splitInstallManager = SplitInstallManagerFactory.create(requireContext())
        splitInstallManager.installedModules.let {
            logcat { "installedModules=${it.joinToString(", ")}" }
        }
    }

    private fun request() {
        val splitInstallManager = SplitInstallManagerFactory.create(requireContext())
        val request = SplitInstallRequest
            .newBuilder()
            .addModule("pdf_support")
            .build()
        splitInstallManager
            .startInstall(request)
            .addOnSuccessListener { sessionId ->
                Toast.makeText(requireContext(), "sessionId", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage.orEmpty().ifEmpty { "exception" }, Toast.LENGTH_LONG).show()
                logcat { exception.localizedMessage.orEmpty().ifEmpty { "exception" } }
            }
    }
}
