package yet.ui.res

import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat
import yet.ext.ColorListLight
import yet.theme.Colors
import yet.ui.ext.dp

/**
 * Created by entaoyang@163.com on 2018-03-07.
 */

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