package com.sorrowblue.comicviewer.framework.ui.navigation

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import java.io.Serializable

interface SupportSafeArgs {
    val savedStateHandle: SavedStateHandle
}

inline fun <reified Args : NavArgs> SupportSafeArgs.navArgs() = NavArgsLazy(Args::class) {
    val bundle = Bundle()
    savedStateHandle.keys().forEach {
        val value = savedStateHandle.get<Any>(it)
        if (value is Serializable) {
            bundle.putSerializable(it, value)
        } else if (value is Parcelable) {
            bundle.putParcelable(it, value)
        }
    }
    bundle
}
