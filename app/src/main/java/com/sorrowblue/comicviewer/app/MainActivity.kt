package com.sorrowblue.comicviewer.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.color.DynamicColors
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitcompat.SplitCompat
import com.sorrowblue.comicviewer.app.databinding.ActivityMainBinding
import com.sorrowblue.comicviewer.app.databinding.ViewAuthBinding
import com.sorrowblue.comicviewer.app.ktx.findNavController
import com.sorrowblue.comicviewer.app.ktx.isShown
import com.sorrowblue.comicviewer.app.ktx.isShownWithImageResource
import com.sorrowblue.comicviewer.folder.FolderFragmentArgs
import com.sorrowblue.comicviewer.framework.ui.flow.launchInWithLifecycle
import com.sorrowblue.comicviewer.framework.ui.fragment.CommonViewModel
import com.sorrowblue.comicviewer.framework.ui.fragment.type
import com.sorrowblue.comicviewer.framework.ui.navigation.FrameworkDynamicNavHostFragment
import com.sorrowblue.comicviewer.framework.ui.navigation.FrameworkFragmentNavigator
import com.sorrowblue.comicviewer.tutorial.TutorialFragmentArgs
import com.sorrowblue.jetpack.binding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import logcat.LogPriority
import logcat.logcat

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels()
    private val commonViewModel: CommonViewModel by viewModels()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        logcat("Configuration") { "screenWidthDp=${resources.configuration.screenWidthDp}" }
        logcat("Configuration") { "orientation=${if (Configuration.ORIENTATION_LANDSCAPE == resources.configuration.orientation) "LANDSCAPE" else "PORTRAIT"}" }
        val splashScreen = installSplashScreen()
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)
        splashScreen.setOnExitAnimationListener(SplashScreenViewProvider::startSlideUpAnime)
        splashScreen.setKeepOnScreenCondition {
            commonViewModel.shouldKeepOnScreen
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        commonViewModel.snackbarMessage.onEach {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                .setAnchorView(com.sorrowblue.comicviewer.framework.ui.R.id.framework_ui_fab)
                .apply {
                    isAnchorViewLayoutListenerEnabled = true
                }
                .show()
        }.launchInWithLifecycle()


        val navController =
            binding.navHostFragmentActivityMain.findNavController<FrameworkDynamicNavHostFragment>()
        binding.viewAuth.applyViewModel(viewModel, navController)
        // 初期化処理未実施の場合
        if (commonViewModel.shouldKeepOnScreen) {
            // リストア Skip/完了 を待つ
            lifecycleScope.launch {
                restoreNavigation(navController)
                commonViewModel.isRestored.first()

                if (!runBlocking { viewModel.doneTutorialFlow.first() }) {
                    navController.navigate(
                        MobileNavigationDirections.actionGlobalTutorialNavigation().actionId,
                        TutorialFragmentArgs(R.id.mobile_navigation).toBundle()
                    )
                }
                commonViewModel.shouldKeepOnScreen = false
            }
        }
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemReselectedListener {
            logcat { navController.currentBackStack.value.lastOrNull()?.destination?.parent?.startDestDisplayName.orEmpty() }
            if (navController.currentBackStack.value.lastOrNull()?.destination?.parent?.id != it.itemId) {
                navController.popBackStack()
            }
        }
        commonViewModel.isVisibleBottomNav.onEach {
            binding.bottomNavigation.isShown(it)
        }.launchInWithLifecycle()
        navController.currentBackStack.onEach {
            logcat(
                "NAVIGATION",
                LogPriority.INFO
            ) { it.joinToString(",") { it.destination.displayName.removePrefix("$packageName:") } }
        }.launchInWithLifecycle()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            logcat { destination.hierarchy.joinToString { it.displayName.removePrefix("$packageName:") } }
            if (destination is FrameworkFragmentNavigator.Destination) {
                commonViewModel.isVisibleBottomNav.tryEmit(destination.isVisibleBottomNavigation)
                binding.frameworkUiFab.isShownWithImageResource(
                    destination.isVisibleFab,
                    destination.fabIcon,
                    destination.fabLabel
                )
            } else {
                binding.bottomNavigation.isShown(false)
                binding.frameworkUiFab.isShownWithImageResource(false, 0, 0)
            }
        }
        binding.frame.applyInsetter {
            type(systemBars = true, displayCutout = true) {
                margin(true)
            }
        }

        WorkManager.getInstance(applicationContext).getWorkInfosByTagLiveData("scan").asFlow()
            .onEach { workInfoList ->
                workInfoList.forEach {
                    logcat("WORK") {
                        "id=[${it.id}], state=[${it.state}], progress=[${
                            it.progress.keyValueMap.values.joinToString(
                                ","
                            ) { it.toString() }
                        }]"
                    }
                }
            }.launchInWithLifecycle()

    }

    private suspend fun restoreNavigation(navController: NavController) {
        if (!viewModel.settings.first().restoreOnLaunch) {
            logcat("RESTORE_NAVIGATION", LogPriority.INFO) { "Do not restore navigation." }
            commonViewModel.isRestored.emit(true)
            return
        }
        val (server, bookshelves, position) = viewModel.getNavigationHistory().first()?.triple
            ?: kotlin.run {
                logcat("RESTORE_NAVIGATION", LogPriority.INFO) {
                    "Do not restore navigation(GET_HISTORY_ERROR)."
                }
                commonViewModel.isRestored.emit(true)
                return
            }
        logcat("RESTORE_NAVIGATION", LogPriority.INFO) { "Start restore navigation." }
        if (bookshelves.isNotEmpty()) {
            // library -> folder
            if (bookshelves.size == 1) {
                navController.navigate(
                    com.sorrowblue.comicviewer.bookshelf.R.id.action_bookshelf_list_to_folder,
                    FolderFragmentArgs(
                        server.id.value,
                        bookshelves.first().base64Path(),
                        position = position
                    ).toBundle()
                )
                logcat("RESTORE_NAVIGATION", LogPriority.INFO) {
                    "server(${server.id}) -> folder(${bookshelves.first().path})"
                }
            } else {
                navController.navigate(
                    com.sorrowblue.comicviewer.bookshelf.R.id.action_bookshelf_list_to_folder,
                    FolderFragmentArgs(
                        server.id.value,
                        bookshelves.first().base64Path(),
                        position = position
                    ).toBundle()
                )
                logcat("RESTORE_NAVIGATION", LogPriority.INFO) {
                    "server(${server.id}) -> folder(${bookshelves.first().path})"
                }
                bookshelves.drop(1).dropLast(1).forEachIndexed { index, folder ->
                    // folder -> folder
                    navController.navigate(
                        com.sorrowblue.comicviewer.folder.R.id.action_folder_self,
                        FolderFragmentArgs(server.id.value, folder.base64Path()).toBundle()
                    )
                    logcat("RESTORE_NAVIGATION", LogPriority.INFO) {
                        "folder(${bookshelves[index].path}) -> folder${bookshelves[index + 1].path}"
                    }
                }
                navController.navigate(
                    com.sorrowblue.comicviewer.folder.R.id.action_folder_self,
                    FolderFragmentArgs(
                        server.id.value,
                        bookshelves.last().base64Path(),
                        position = position
                    ).toBundle()
                )
                logcat("RESTORE_NAVIGATION", LogPriority.INFO) {
                    "folder(${
                        bookshelves.dropLast(1).last().path
                    }) -> folder${bookshelves.last().path}"
                }
            }
        } else {
            commonViewModel.isRestored.emit(true)
        }
        logcat("RESTORE_NAVIGATION", LogPriority.INFO) { "Done restore navigation." }
    }
}

context (MainActivity)
internal fun ViewAuthBinding.applyViewModel(
    viewModel: MainViewModel,
    navController: NavController
) {
    password.doAfterTextChanged {
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false
    }
    password.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_GO) {
            logcat { "${password.editableText}=${runBlocking { viewModel.securitySettingsFlow.first() }.password}" }
            if (password.editableText.toString() == runBlocking { viewModel.securitySettingsFlow.first() }.password) {
                navController.navigate(MobileNavigationDirections.actionGlobalBookshelf())
                BottomSheetBehavior.from(authBottomSheet).state = BottomSheetBehavior.STATE_HIDDEN
                WindowInsetsControllerCompat(window, root).hide(WindowInsetsCompat.Type.ime())
                true
            } else {
                textInputLayout.error = "パスワードが間違っています。"
                true
            }
        } else {
            false
        }
    }
    viewModel.securitySettingsFlow.map { it.password != null }.onEach {
        BottomSheetBehavior.from(authBottomSheet).apply {
            expandedOffset = 0
            isDraggable = false
            state = if (it) {
                BottomSheetBehavior.STATE_EXPANDED
            } else {
                BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }.launchInWithLifecycle()
}
