package com.sorrowblue.comicviewer.domain.model.library

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Library(
    private val _id: Int,
    val name: String,
    val host: String,
    val path: String,
    val port: String,
    val protocol: SupportProtocol,
    val username: String,
    val password: String,
    val preview: List<String> = emptyList()
) : Parcelable {

    constructor(
        name: String,
        host: String,
        path: String,
        port: String,
        protocol: SupportProtocol,
        username: String,
        password: String
    ) : this(0, name, host, path, port, protocol, username, password)

    @IgnoredOnParcel
    val id
        get() = LibraryId(_id)
}
