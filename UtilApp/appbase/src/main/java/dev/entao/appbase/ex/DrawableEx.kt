package dev.entao.appbase.ex

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat


fun ColorDrawable(normal: Int, pressed: Int): Drawable {
    return ColorStated(normal).pressed(pressed).selected(pressed).focused(pressed).value
}

fun ColorListLight(normal: Int, pressed: Int): ColorStateList {
    return ColorList(normal).pressed(pressed).selected(pressed).focused(pressed).value
}



fun Drawable.limited(maxEdge: Int): Drawable {
    val h = this.intrinsicHeight
    val w = this.intrinsicWidth
    if (w > maxEdge || h > maxEdge) {
        if (w > h) {
            this.setBounds(0, 0, dp(maxEdge), dp(h * maxEdge / w))
        } else {
            this.setBounds(0, 0, dp(w * maxEdge / h), dp(maxEdge))
        }
    }
    return this
}

fun Drawable.sized(w: Int, h: Int = w): Drawable {
    this.setBounds(0, 0, dp(w), dp(h))
    return this
}

val Drawable.tintedWhite: Drawable
    get() {
        return this.tinted(Colors.WHITE)
    }

fun Drawable.tinted(color: Int): Drawable {
    val dc = DrawableCompat.wrap(this.mutate())
    DrawableCompat.setTint(dc, color)
    return dc
}

fun Drawable.tinted(color: Int, light: Int): Drawable {
    val dc = DrawableCompat.wrap(this.mutate())
    DrawableCompat.setTintList(dc, ColorListLight(color, light))
    return dc
}