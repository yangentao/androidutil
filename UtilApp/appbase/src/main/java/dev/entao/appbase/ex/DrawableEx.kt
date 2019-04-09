package dev.entao.appbase.ex

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable


fun ColorDrawable(normal: Int, pressed: Int): Drawable {
    return ColorStated(normal).pressed(pressed).selected(pressed).focused(pressed).value
}

fun ColorListLight(normal: Int, pressed: Int): ColorStateList {
    return ColorList(normal).pressed(pressed).selected(pressed).focused(pressed).value
}
