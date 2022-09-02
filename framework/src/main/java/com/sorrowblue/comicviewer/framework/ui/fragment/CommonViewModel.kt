package com.sorrowblue.comicviewer.framework.ui.fragment

import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class CommonViewModel @Inject constructor() : ViewModel() {

    val isVisibleToolbar = MutableStateFlow(true)
    val menu = MutableStateFlow(View.NO_ID)

    var restore = true
}
