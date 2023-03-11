package com.sorrowblue.comicviewer.book

import com.sorrowblue.comicviewer.domain.entity.file.Book

fun BookFragmentArgs(book: Book, transitionName: String? = null) =
    BookFragmentArgs(book.bookshelfId.value, book.base64Path(), transitionName, book.lastPageRead)
