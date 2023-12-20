package com.sorrowblue.comicviewer.feature.favorite.add.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.sorrowblue.comicviewer.domain.model.Base64.decodeFromBase64
import com.sorrowblue.comicviewer.domain.model.Base64.encodeToBase64
import com.sorrowblue.comicviewer.domain.model.bookshelf.BookshelfId
import com.sorrowblue.comicviewer.domain.model.file.File
import com.sorrowblue.comicviewer.feature.favorite.add.FavoriteAddRoute

private const val BookshelfIdArg = "bookshelfId"
private const val PathArg = "path"

internal class FavoriteAddArgs(
    val bookshelfId: BookshelfId,
    val path: String,
) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        BookshelfId(checkNotNull(savedStateHandle[BookshelfIdArg])),
        (checkNotNull(savedStateHandle[PathArg]) as String).decodeFromBase64(),
    )
}

private const val FavoriteAddRoute = "favorite/add"

fun NavGraphBuilder.favoriteAddScreen(
    isMobile: Boolean,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
) {
    if (isMobile) {
        composable(
            route = "$FavoriteAddRoute?$BookshelfIdArg={$BookshelfIdArg}&$PathArg={$PathArg}",
            arguments = listOf(
                navArgument(BookshelfIdArg) { type = NavType.IntType },
                navArgument(PathArg) { type = NavType.StringType }

            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(700)
                )
            }
        ) { navBackStackEntry ->
            with(navBackStackEntry) {
                FavoriteAddRoute(
                    onBackClick = onBackClick,
                    contentPadding = contentPadding
                )
            }
        }
    } else {
        dialog(
            route = "$FavoriteAddRoute?$BookshelfIdArg={$BookshelfIdArg}&$PathArg={$PathArg}",
            arguments = listOf(
                navArgument(BookshelfIdArg) { type = NavType.IntType },
                navArgument(PathArg) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            with(navBackStackEntry) {
                FavoriteAddRoute(
                    onBackClick = onBackClick,
                    contentPadding = contentPadding
                )
            }
        }
    }
}

fun NavController.navigateToFavoriteAdd(
    file: File,
    navOptions: NavOptions? = null,
) {
    navigate(
        "$FavoriteAddRoute?$BookshelfIdArg=${file.bookshelfId.value}&$PathArg=${file.path.encodeToBase64()}",
        navOptions
    )
}
