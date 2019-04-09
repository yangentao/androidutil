@file:Suppress("unused", "FunctionName")

package dev.entao.appbase.ex

import android.graphics.Color

val Long.color: Int
    get() {
        if (this <= 0xffffff) {
            val L = 0xff000000 or this
            return L.toInt()
        }
        if (this <= 0xffffffff) {
            return toInt()
        }
        throw IllegalArgumentException("不是颜色值:${this.toString(16)}")
    }

//0x80A0B0C0, Color.argb(0x80,0xA0,0xB0,0XC0)
val Long.argb: Int
    get() {
        return (this and 0xFFFFFFFFL).toInt()
    }

val Long.rgb: Int
    get() {
        return ((this and 0x00FFFFFFL) or 0xFF000000).toInt()
    }

val Int.color: Int
    get() {
        return this
    }

fun grayColor(n: Int): Int {
    return Color.rgb(n, n, n)
}

val Int.grayColor get() = grayColor(this)

fun RGB(r: Int, g: Int, b: Int): Int {
    return Color.rgb(r, g, b)
}

fun ARGB(a: Int, r: Int, g: Int, b: Int): Int {
    return Color.argb(a, r, g, b)
}

//#8f8, #800f, #ff884422
fun colorParse(colorString: String): Int {
    if (colorString[0] == '#') {
        val len = colorString.length
        if (len == 4 || len == 5) {
            val sb = StringBuffer(10)
            sb.append('#')
            for (i in 1 until len) {
                sb.append(colorString[i])
                sb.append(colorString[i])
            }
            return Color.parseColor(sb.toString())
        }
    }
    return Color.parseColor(colorString)
}