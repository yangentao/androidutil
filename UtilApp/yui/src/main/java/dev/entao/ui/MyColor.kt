package dev.entao.ui

import android.graphics.Color

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */

class MyColor(val color: Int) {

	val red: Int get() = Color.red(color)
	val green: Int get() = Color.green(color)
	val blue: Int get() = Color.blue(color)
	val alpha: Int get() = Color.alpha(color)


	fun multiRGB(f: Double): Int {
		return dev.entao.ui.MyColor.Companion.argb(alpha, (red * f).toInt(), (green * f).toInt(), (blue * f).toInt())
	}


	companion object {
		fun rgb(r: Int, g: Int, b: Int): Int {
			return Color.rgb(r, g, b)
		}

		fun argb(a: Int, r: Int, g: Int, b: Int): Int {
			return Color.argb(a, r, g, b)
		}
	}
}