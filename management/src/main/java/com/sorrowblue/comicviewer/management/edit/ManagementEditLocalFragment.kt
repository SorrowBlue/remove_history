package com.sorrowblue.comicviewer.management.edit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sorrowblue.comicviewer.framework.ui.fragment.FrameworkFragment
import com.sorrowblue.comicviewer.management.R
import com.sorrowblue.comicviewer.management.databinding.ManagementFragmentEditLocalBinding
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.outputStream
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
internal class ManagementEditLocalFragment :
    FrameworkFragment(R.layout.management_fragment_edit_local) {

    private val binding: ManagementFragmentEditLocalBinding by viewBinding()
    private val viewModel: ManagementEditLocalViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.select.setOnClickListener {
            openDirectory()
        }
        binding.fab.setOnClickListener {  }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.result.collectLatest {
                logcat { "viewModel.result = $it" }
                findNavController().popBackStack()
            }
        }
    }


    val c = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        binding.dir.text = DocumentFile.fromTreeUri(requireContext(), it.data?.data!!)?.path()
        logcat { "registerForActivityResult: ${it.data!!.data!!}" }
        logcat { "registerForActivityResult: ${it.data!!.data!!.host + it.data!!.data!!.encodedPath}" }
        requireContext().contentResolver.takePersistableUriPermission(it.data!!.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        viewModel.connect(DocumentFile.fromTreeUri(requireContext(), it.data?.data!!)!!) {

        }
        logcat {
            "files: ${
                DocumentFile.fromTreeUri(requireContext(), it.data?.data!!)?.listFiles()
                    ?.mapNotNull { it.name }?.joinToString(",")
            }"
        }
    }

    fun openDirectory() {
        // Choose a directory using the system's file picker.
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }
        c.launch(intent)
    }

    fun DocumentFile.path(): String {
        return parentFile?.let { it.path() + File.separator + name } ?: name.orEmpty()
    }

}
