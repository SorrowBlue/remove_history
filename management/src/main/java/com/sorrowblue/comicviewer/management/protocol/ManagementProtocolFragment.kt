package com.sorrowblue.comicviewer.management.protocol

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.sorrowblue.comicviewer.framework.ui.fragment.FrameworkFragment
import com.sorrowblue.comicviewer.domain.model.library.SupportProtocol
import com.sorrowblue.comicviewer.management.R
import com.sorrowblue.comicviewer.management.databinding.ManagementFragmentProtocolBinding
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ManagementProtocolFragment : FrameworkFragment(R.layout.management_fragment_protocol) {

    private val binding: ManagementFragmentProtocolBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ManagementProtocolAdapter {
            when (it) {
                SupportProtocol.SMB ->  findNavController().navigate(
                    ManagementProtocolFragmentDirections.actionManagementProtocolToManagementNavigation()
                )
                SupportProtocol.Local -> findNavController().navigate(
                    ManagementProtocolFragmentDirections.actionManagementProtocolToManagementLocalNavigation()
                )
            }
        }
        binding.recyclerView.adapter = adapter
        adapter.submitList(SupportProtocol.values().asList())
    }
}
