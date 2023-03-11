package com.sorrowblue.comicviewer.library.box.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.sorrowblue.comicviewer.library.box.R
import com.sorrowblue.comicviewer.library.box.databinding.BoxFragmentProfileBinding
import com.sorrowblue.jetpack.binding.viewBinding

class BoxProfileFragment : DialogFragment(R.layout.box_fragment_profile) {

    private val binding: BoxFragmentProfileBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
