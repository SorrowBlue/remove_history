package com.sorrowblue.comicviewer.framework.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sorrowblue.comicviewer.framework.R

open class FrameworkFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {

    protected val commonViewModel: CommonViewModel by activityViewModels()

    protected val bottomAppBar get() = requireActivity().requireViewById<BottomAppBar>(R.id.bottom_app_bar)
    protected val toolbar get() = requireActivity().requireViewById<MaterialToolbar>(R.id.toolbar)
}
