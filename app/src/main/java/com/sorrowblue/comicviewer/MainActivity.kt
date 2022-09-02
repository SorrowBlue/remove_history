package com.sorrowblue.comicviewer

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.database.getStringOrNull
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.color.DynamicColors
import com.sorrowblue.comicviewer.bookshelf.BookshelfFragmentArgs
import com.sorrowblue.comicviewer.databinding.ActivityMainBinding
import com.sorrowblue.comicviewer.framework.ui.fragment.CommonViewModel
import com.sorrowblue.comicviewer.framework.ui.navigation.FrameworkFragmentNavigator
import com.sorrowblue.comicviewer.framework.ui.navigation.FrameworkNavHostFragment
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import logcat.logcat

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main),
    NavController.OnDestinationChangedListener {

    private val binding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()
    private val commonViewModel: CommonViewModel by viewModels()
    private val navController get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as FrameworkNavHostFragment).navController

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            viewModel.shouldKeepOnScreen
        }
        DynamicColors.applyToActivityIfAvailable(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                com.sorrowblue.comicviewer.library.R.id.library_fragment
            )
        )
        binding.commonViewModel = commonViewModel
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.setOnMenuItemClickListener {
            it.onNavDestinationSelected(navController)
        }
        lifecycleScope.launch {
            commonViewModel.menu.collectLatest {
                binding.toolbar.menu.clear()
                if (it != View.NO_ID)
                binding.toolbar.inflateMenu(it)
            }
        }
        navController.addOnDestinationChangedListener(this)
        lifecycleScope.launch {
            viewModel.list.flowWithLifecycle(lifecycle).collectLatest {
                val (bookshelf, file) = it
                if (bookshelf != null) {
                    // library -> bookshelf
                    navController.navigate(
                        com.sorrowblue.comicviewer.library.R.id.action_library_to_bookshelf_navigation,
                        BookshelfFragmentArgs(bookshelf, null).toBundle()
                    )
                    file.forEach {
                        // bookshelf -> bookshelf
                        navController.navigate(
                            com.sorrowblue.comicviewer.bookshelf.R.id.action_bookshelf_fragment_self,
                            BookshelfFragmentArgs(bookshelf, it).toBundle()
                        )
                    }
                }
                viewModel.shouldKeepOnScreen = false
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        commonViewModel.isVisibleToolbar.value = true
        if (destination is FrameworkFragmentNavigator.Destination) {
            commonViewModel.menu.value = destination.menu
        }
    }
}
