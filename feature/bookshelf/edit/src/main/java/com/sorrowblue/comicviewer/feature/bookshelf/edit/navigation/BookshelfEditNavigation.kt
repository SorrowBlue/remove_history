package com.sorrowblue.comicviewer.feature.bookshelf.edit.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import com.sorrowblue.comicviewer.domain.model.bookshelf.BookshelfId
import com.sorrowblue.comicviewer.domain.model.bookshelf.BookshelfType
import com.sorrowblue.comicviewer.feature.bookshelf.edit.BookshelfEditScreen
import com.sorrowblue.comicviewer.framework.ui.ComposeValue

internal const val BookshelfIdArg = "bookshelfId"
internal const val BookshelfTypeArg = "bookshelfType"

private const val BookshelfEditRouteBase = "bookshelf/edit"
const val BookshelfEditRoute =
    "$BookshelfEditRouteBase?bookshelf_id={$BookshelfIdArg}&type={$BookshelfTypeArg}"

internal class BookshelfEditArgs(val bookshelfId: BookshelfId, val bookshelfType: BookshelfType) {

    constructor(bundle: Bundle) : this(
        BookshelfId(checkNotNull(bundle.getInt(BookshelfIdArg))),
        BookshelfType.valueOf(checkNotNull(bundle.getString(BookshelfTypeArg)))
    )
}

fun NavController.navigateToBookshelfEdit(
    bookshelfId: BookshelfId,
    navOptions: NavOptions? = null,
) {
    navigate("$BookshelfEditRouteBase?bookshelf_id=${bookshelfId.value}", navOptions)
}

fun NavController.navigateToBookshelfEdit(
    bookshelfType: BookshelfType,
    navOptions: NavOptions? = null,
) {
    navigate("$BookshelfEditRouteBase?type=${bookshelfType.name}", navOptions)
}

context(ComposeValue)
fun NavGraphBuilder.bookshelfEditScreen(
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
) {
    if (isCompact) {
        composable(
            route = BookshelfEditRoute,
            arguments = listOf(
                navArgument(BookshelfIdArg) {
                    type = NavType.IntType
                    defaultValue = BookshelfId.Default
                },
                navArgument(BookshelfTypeArg) {
                    type = NavType.StringType
                    defaultValue = BookshelfType.DEVICE.name
                },
            )
        ) {
            BookshelfEditScreen(
                args = BookshelfEditArgs(it.arguments!!),
                onBackClick = onBackClick,
                onComplete = onComplete,
                contentPadding = contentPadding
            )
        }
    } else {
        dialog(
            route = BookshelfEditRoute,
            arguments = listOf(
                navArgument(BookshelfIdArg) {
                    type = NavType.IntType
                    defaultValue = BookshelfId.Default
                },
                navArgument(BookshelfTypeArg) {
                    type = NavType.StringType
                    defaultValue = BookshelfType.DEVICE.name
                },
            )
        ) {
            BookshelfEditScreen(
                args = BookshelfEditArgs(it.arguments!!),
                onBackClick = onBackClick,
                onComplete = onComplete,
                contentPadding = contentPadding
            )
        }
    }
}
