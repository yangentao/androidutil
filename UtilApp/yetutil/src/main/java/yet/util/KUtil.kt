package yet.util

import android.net.Uri
import android.os.Build
import dev.entao.appbase.App
import dev.entao.log.loge
import yet.util.app.FileProv
import java.io.File

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */


val debug: Boolean by lazy { App.debug }


fun debugThrow(msg: String) {
    loge(msg)
    if (debug) {
        throw IllegalStateException(msg)
    }
}

/**
 * null, false, 空字符串,空集合, 空数组, 空map都认定为空,  返回true
 */
fun empty(obj: Any?): Boolean {
    if (obj == null) {
        return true
    }
    return when (obj) {
        is String -> obj.length == 0
        is Boolean -> !obj
        is Float, Double -> obj == 0
        is Number -> obj.toInt() == 0
        is Collection<*> -> obj.size == 0
        is Map<*, *> -> obj.size == 0
        is Array<*> -> obj.size == 0
        else -> false
    }
}

fun <T> emptyOr(obj: T?, v: T): T {
    return if (empty(obj)) v else obj!!
}

fun <T> nullOr(obj: T?, v: T): T {
    return obj ?: v
}


fun notEmpty(obj: Any?): Boolean {
    return !empty(obj)
}

fun OR(vararg objs: Any?): Any? {
    for (obj in objs) {
        if (notEmpty(obj)) {
            return obj
        }
    }
    return null
}

fun AND(vararg objs: Any?): Boolean {
    for (obj in objs) {
        if (empty(obj)) {
            return false
        }
    }
    return objs.size > 0
}


fun ByteArray?.prefix(vararg bs: Byte): Boolean {
    if (this == null) {
        return false
    }
    if (this.size < bs.size) {
        return false
    }
    for (i: Int in bs.indices) {
        if (this[i] != bs[i]) {
            return false
        }
    }
    return true
}

fun ByteArray?.skip(n: Int): ByteArray {
    if (this == null || this.size <= n) {
        return byteArrayOf()
    }
    val arr = ByteArray(this.size - n)
    for (i in this.indices) {
        if (i >= n) {
            arr[i - n] = this[i]
        }
    }
    return arr
}


fun UriFromSdFile(file: File): Uri {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Uri.fromFile(file)
    } else {
        FileProv.uriOfFile(file)
    }
}



