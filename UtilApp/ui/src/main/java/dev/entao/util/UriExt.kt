package dev.entao.util

import android.net.Uri
import android.webkit.MimeTypeMap
import dev.entao.util.app.MediaInfo

/**
 * Created by entaoyang@163.com on 2017-03-19.
 */



val Uri.extName: String?
    get() {
        val s = this.fileName
        if (s != null) {
            val ext = s.substringAfterLast('.', "")
            if (ext.isNotEmpty()) {
                return ext
            }
        }
        return null
    }
val Uri.mimeType: String?
    get() {
        return MimeOf(this)
    }
val Uri.fileName: String?
    get() {
        return FileNameOf(this)
    }

fun MimeOf(uri: Uri): String? {
    if (uri.scheme == "content") {
        if (uri.host == "media") {
            return MediaInfo(uri).mimeType
        }
    }
    val filename = uri.lastPathSegment
    val n = filename.lastIndexOf('.')
    if (n > 0) {
        val ext = filename.substring(n + 1)
        if (ext.isNotEmpty()) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
        }
    }
    return null
}

fun FileNameOf(uri: Uri): String? {
    if (uri.scheme == "content") {
        if (uri.host == "media") {
            return MediaInfo(uri).displayName
        }
    }
    return uri.lastPathSegment
}