package com.sorrowblue.comicviewer.app

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import com.slack.circuit.backstack.BackStack
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.sorrowblue.comicviewer.bookshelf.BookshelfPresenter
import com.sorrowblue.comicviewer.bookshelf.BookshelfPresenterFactory
import com.sorrowblue.comicviewer.bookshelf.BookshelfScreen
import com.sorrowblue.comicviewer.bookshelf.BookshelfScreenFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {

    private val viewModel: ComicViewerAppViewModel by viewModels()

    @Inject
    lateinit var bookshelfPresenterFactory: BookshelfPresenterFactory

    @Inject
    lateinit var bookshelfScreenFactory: BookshelfScreenFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            enableEdgeToEdge(
                navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            )
            super.onCreate(savedInstanceState)
//            setKeepOnScreenCondition(viewModel::shouldKeepSplash)
//            setOnExitAnimationListener(SplashScreenViewProvider::startSlideUpAnime)
        }

//        setContent {
//            val navController = rememberNavController()
//            ComicViewerApp(
//                onTutorial = {
//                    navController.navigate(TutorialNavGraph) {
//                        popUpTo(RootNavGraph) {
//                            inclusive = true
//                        }
//                    }
//                },
//                navController = navController
//            )
//        }

        val circuit: Circuit =
            Circuit.Builder()
                .addPresenterFactory(bookshelfPresenterFactory)
                .addUiFactory(bookshelfScreenFactory)
//                .addPresenterFactory(detailPresenterFactory)
//                .addUiFactory(detailUiFactory)
                .build()

        setContent {
            val backStack: BackStack<SaveableBackStack.Record> = rememberSaveableBackStack(root = BookshelfScreen)
            val navigator = rememberCircuitNavigator(backStack) {

                // Do something when the root screen is popped, usually exiting the app
            }

            CircuitCompositionLocals(circuit) {
                NavigableCircuitContent(navigator = navigator, backStack = backStack)
//                CircuitContent(InboxScreen)
            }
        }
    }
}

private fun SplashScreenViewProvider.startSlideUpAnime() {
    kotlin.runCatching {
        val slideUp = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f, -iconView.height * 2f)
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.doOnEnd { remove() }
        slideUp.duration =
            if (iconAnimationDurationMillis - System.currentTimeMillis() + iconAnimationStartMillis < 0) 300 else iconAnimationDurationMillis - System.currentTimeMillis() + iconAnimationStartMillis
        slideUp.start()
    }.onFailure { remove() }
}
