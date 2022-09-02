package com.sorrowblue.comicviewer.data.remote

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sorrowblue.comicviewer.data.entity.FileData
import com.sorrowblue.comicviewer.data.entity.LibraryData
import com.sorrowblue.comicviewer.data.remote.communication.SmbFileClient
import java.security.MessageDigest
import java.security.Provider
import java.security.Security
import java.time.LocalDateTime
import kotlin.io.path.outputStream
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper
import org.apache.commons.compress.archivers.zip.ZipFile
import org.junit.Test
import org.junit.runner.RunWith
import org.mozilla.universalchardet.UniversalDetector

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
    }
}
