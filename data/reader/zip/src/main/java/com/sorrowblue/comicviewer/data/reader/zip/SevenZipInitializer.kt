package com.sorrowblue.comicviewer.data.reader.zip

import androidx.annotation.Keep
import logcat.LogPriority
import logcat.logcat
import net.sf.sevenzipjbinding.SevenZip

@Keep
internal object SevenZipInitializer {

    fun init() {
        SevenZip.initSevenZipFromPlatformJAR()
        logcat(LogPriority.INFO) { "Initialized SevenZip. Version is ${SevenZip.getSevenZipJBindingVersion()}." }
    }
}
