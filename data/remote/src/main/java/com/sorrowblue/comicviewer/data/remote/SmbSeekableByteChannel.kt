package com.sorrowblue.comicviewer.data.remote

import java.nio.ByteBuffer
import java.nio.channels.ClosedChannelException
import java.nio.channels.SeekableByteChannel
import jcifs.smb.SmbFile
import jcifs.smb.SmbRandomAccessFile

internal class SmbSeekableByteChannel(
    file: SmbFile,
    write: Boolean,
    create: Boolean,
    create_new: Boolean,
    truncate: Boolean,
    append: Boolean
) : SeekableByteChannel {

    private var random: SmbRandomAccessFile? = null

    @Volatile
    private var open = true

    init {
        if (create || create_new) {
            if (file.exists()) {
                if (create_new) throw RuntimeException("The specified file '" + file.path.toString() + "' does already exist!")
            } else {
                file.createNewFile()
            }
        }
        if (write) {
            random = SmbRandomAccessFile(file, "rw")
            if (truncate) random!!.setLength(0)
            if (append) random!!.seek(random!!.length())
        } else {
            random = SmbRandomAccessFile(file, "r")
        }
    }

    @Synchronized
    override fun read(dst: ByteBuffer): Int {
        if (!open) throw ClosedChannelException()
        val len = dst.limit() - dst.position()
        val buffer = ByteArray(len)
        val read = random!!.read(buffer)
        if (read > 0) dst.put(buffer, 0, read)
        return read
    }

    @Synchronized
    override fun write(src: ByteBuffer): Int {
        if (!open) throw ClosedChannelException()
        val len = src.limit() - src.position()
        val buffer = ByteArray(len)
        src[buffer]
        random!!.write(buffer)
        return len
    }

    @Synchronized
    override fun position(): Long {
        if (!open) throw ClosedChannelException()
        return random!!.filePointer
    }

    @Synchronized
    override fun size(): Long {
        if (!open) throw ClosedChannelException()
        return random!!.length()
    }

    @Synchronized
    override fun position(newPosition: Long): SeekableByteChannel {
        if (!open) throw ClosedChannelException()
        random!!.seek(newPosition)
        return this
    }

    @Synchronized
    override fun truncate(size: Long): SeekableByteChannel {
        if (!open) throw ClosedChannelException()
        random!!.setLength(size)
        return this
    }

    @Synchronized
    override fun isOpen(): Boolean {
        return open
    }

    @Synchronized
    override fun close() {
        if (!open) return
        open = false
        random!!.close()
    }
}
