package com.sorrowblue.comicviewer.framework.ui.navigation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.FragmentNavigator
import com.sorrowblue.comicviewer.framework.ui.R

@Navigator.Name("fragment")
class FrameworkFragmentNavigator(
    context: Context,
    manager: FragmentManager,
    containerId: Int,
) : FragmentNavigator(context, manager, containerId) {

    override fun createDestination(): Destination = Destination(this)

    class Destination : FragmentNavigator.Destination {

        var isVisibleBottomNavigation: Boolean = false
            private set
        var isVisibleFab: Boolean = false
            private set
        var fabIcon: Int = View.NO_ID
            private set
        var fabLabel: Int = View.NO_ID
            private set

        @Suppress("unused")
        constructor(navigatorProvider: NavigatorProvider) : super(navigatorProvider)

        constructor(
            fragmentNavigator: Navigator<out FragmentNavigator.Destination>
        ) : super(fragmentNavigator)

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)
            context.withStyledAttributes(attrs, R.styleable.FrameworkDynamicFragmentNavigator) {
                isVisibleBottomNavigation = getBoolean(
                    R.styleable.FrameworkDynamicFragmentNavigator_bottomNavVisible,
                    false
                )
                isVisibleFab =
                    getBoolean(R.styleable.FrameworkDynamicFragmentNavigator_fabVisible, false)
                fabIcon =
                    getResourceId(R.styleable.FrameworkDynamicFragmentNavigator_fabIcon, View.NO_ID)
                fabLabel = getResourceId(
                    R.styleable.FrameworkDynamicFragmentNavigator_fabLabel,
                    View.NO_ID
                )
            }
        }
    }
}
