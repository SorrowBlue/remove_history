package com.sorrowblue.comicviewer.app

import com.ramcosta.composedestinations.annotation.ExternalDestination
import com.ramcosta.composedestinations.annotation.ExternalNavGraph
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.sorrowblue.comicviewer.feature.book.navgraphs.BookNavGraph
import com.sorrowblue.comicviewer.feature.bookshelf.navgraphs.BookshelfNavGraph
import com.sorrowblue.comicviewer.feature.favorite.add.destinations.FavoriteAddScreenDestination
import com.sorrowblue.comicviewer.feature.favorite.navgraphs.FavoriteNavGraph
import com.sorrowblue.comicviewer.feature.library.navigation.LibraryNavGraph
import com.sorrowblue.comicviewer.feature.readlater.navgraphs.ReadLaterNavGraph
import com.sorrowblue.comicviewer.feature.search.navgraphs.SearchNavGraph
import com.sorrowblue.comicviewer.feature.settings.navgraphs.SettingsNavGraph
import com.sorrowblue.comicviewer.feature.tutorial.navgraphs.TutorialNavGraph

@NavHostGraph
internal annotation class MainGraph {

    @ExternalNavGraph<BookshelfNavGraph>(start = true)
    @ExternalNavGraph<FavoriteNavGraph>
    @ExternalNavGraph<ReadLaterNavGraph>
    @ExternalNavGraph<SettingsNavGraph>
    @ExternalNavGraph<BookNavGraph>
    @ExternalNavGraph<SearchNavGraph>
    @ExternalNavGraph<LibraryNavGraph>
    @ExternalNavGraph<TutorialNavGraph>
    @ExternalDestination<FavoriteAddScreenDestination>
    companion object Includes
}
