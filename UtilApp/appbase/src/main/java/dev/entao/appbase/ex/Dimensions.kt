package dev.entao.appbase.ex

import android.content.Context
import dev.entao.appbase.App
import dev.entao.appbase.App.displayMetrics

val Int.dp: Int get() = App.dp2px(this)
val Int.sp: Int get() = App.sp2px(this.toFloat())

fun dp(n: Int): Int {
    return n.dp
}


fun Context.sp(sp: Int): Float {
    return sp * displayMetrics.scaledDensity + 0.5f
}


fun sp(n: Int): Int {
    return n.sp
}

fun px2dp(px: Int): Int {
    return App.px2dp(px)
}