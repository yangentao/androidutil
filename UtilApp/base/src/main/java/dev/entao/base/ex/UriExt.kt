package dev.entao.base.ex

import android.content.ContentUris
import android.net.Uri
import java.io.File


val File.uri: Uri get() = Uri.fromFile(this)



fun Uri.appendId(id: Long): Uri {
    return ContentUris.withAppendedId(this, id)
}

fun Uri.appendPath(pathSegment: String): Uri {
    return Uri.withAppendedPath(this, Uri.encode(pathSegment))
}

fun Uri.appendParam(key: String, value: String): Uri {
    return this.buildUpon().appendQueryParameter(key, value).build()
}

fun Uri.parseId(): Long {
    return ContentUris.parseId(this)
}


