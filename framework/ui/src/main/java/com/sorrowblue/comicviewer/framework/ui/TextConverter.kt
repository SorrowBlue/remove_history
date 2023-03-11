package com.sorrowblue.comicviewer.framework.ui

import android.content.Context
import logcat.logcat

object TextConverter {

    @JvmStatic
    fun fileSize(context: Context, fileSize: Long): String {
        return if (fileSize < 1024) {
            context.getString(R.string.label_file_size_b, fileSize.toString())
        } else {
            val kb = fileSize / 1024f
            if (kb < 1024) {
                context.getString(R.string.label_file_size_kb, "%.2f".format(kb))
            } else {
                val mb = kb / 1024f
                if (mb < 1024) {
                    context.getString(R.string.label_file_size_mb, "%.2f".format(mb))
                } else {
                    context.getString(R.string.label_file_size_gb, "%.2f".format(mb / 1024f))
                }
            }
        }.also {
            logcat("TextConverter") { "fileSize($fileSize)=$it" }
        }
    }
}
