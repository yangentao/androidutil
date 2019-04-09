package dev.entao.appbase.ex

import android.net.Uri
import dev.entao.appbase.App
import java.io.FileInputStream
import java.io.InputStream

fun UriRes(resId: Int): Uri {
    return Uri.parse("android.resource://" + App.packageName + "/" + resId.toString())
}

fun Uri.openInputStream(): InputStream {
    if (this.scheme == "content") {
        return App.contentResolver.openInputStream(this)
    }
    return FileInputStream(this.path)
}