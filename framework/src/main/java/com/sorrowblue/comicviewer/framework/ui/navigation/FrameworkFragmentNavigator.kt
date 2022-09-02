package com.sorrowblue.comicviewer.framework.ui.navigation

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import androidx.navigation.NavigatorProvider
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.plusAssign
import com.sorrowblue.comicviewer.framework.R

@Navigator.Name("framework-fragment")
class FrameworkFragmentNavigator(
    context: Context,
    fragmentManager: FragmentManager,
    containerId: Int
) : FragmentNavigator(context, fragmentManager, containerId) {

    override fun createDestination(): Destination {
        return Destination(this)
    }

    @NavDestination.ClassType(Fragment::class)
    class Destination(fragmentNavigator: Navigator<out FragmentNavigator.Destination>) :
        FragmentNavigator.Destination(fragmentNavigator) {

        constructor(navigatorProvider: NavigatorProvider) :
                this(navigatorProvider.getNavigator(FrameworkFragmentNavigator::class.java))

        var isVisibleToolBar = true
        var menu = View.NO_ID

        @CallSuper
        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)
            context.resources.obtainAttributes(attrs, R.styleable.FrameworkFragmentNavigator).use { array ->
                isVisibleToolBar = array.getBoolean(R.styleable.FrameworkFragmentNavigator_isVisibleToolBar, false)
                menu = array.getResourceId(R.styleable.FrameworkFragmentNavigator_menu, View.NO_ID)
            }
        }
    }

}

class FrameworkNavHostFragment : NavHostFragment() {
    private val containerId: Int
        get() {
            val id = id
            return if (id != 0 && id != View.NO_ID) {
                id
            } else androidx.navigation.fragment.R.id.nav_host_fragment_container
            // Fallback to using our own ID if this Fragment wasn't added via
            // add(containerViewId, Fragment)
        }
    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)
        navController.navigatorProvider +=
            FrameworkFragmentNavigator(requireContext(), childFragmentManager, containerId)
    }
}
