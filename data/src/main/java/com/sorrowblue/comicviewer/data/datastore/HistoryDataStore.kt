package com.sorrowblue.comicviewer.data.datastore

import androidx.datastore.core.Serializer
import com.sorrowblue.comicviewer.domain.model.settings.BookshelfSettings
import com.sorrowblue.comicviewer.domain.model.History
import com.sorrowblue.comicviewer.domain.model.settings.Settings
import java.io.InputStream
import java.io.OutputStream
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

object HistorySerializer : Serializer<History> {
    override val defaultValue: History = History()
    override suspend fun readFrom(input: InputStream): History {
        return ProtoBuf.decodeFromByteArray(input.readBytes())
    }

    override suspend fun writeTo(t: History, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}

object BookShelfSettingsSerializer : Serializer<BookshelfSettings> {
    override val defaultValue = BookshelfSettings()
    override suspend fun readFrom(input: InputStream): BookshelfSettings {
        return ProtoBuf.decodeFromByteArray(input.readBytes())
    }

    override suspend fun writeTo(t: BookshelfSettings, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue = Settings()
    override suspend fun readFrom(input: InputStream): Settings {
        return ProtoBuf.decodeFromByteArray(input.readBytes())
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}
