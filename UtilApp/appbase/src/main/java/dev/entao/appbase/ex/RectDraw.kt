package dev.entao.appbase.ex

import android.graphics.drawable.GradientDrawable
import dev.entao.appbase.ex.dp

/**
 * Created by entaoyang@163.com on 16/7/12.
 */


class RectDraw(fillColor: Int? = null) {
	val value: GradientDrawable = GradientDrawable()

	init {
		value.shape = GradientDrawable.RECTANGLE
		if (fillColor != null) {
			value.setColor(fillColor)
		}
	}

	fun color(color: Int): RectDraw {
		value.setColor(color)
		return this
	}

	fun corner(corner: Int): RectDraw {
		value.cornerRadius = dp(corner).toFloat()
		return this
	}

	fun corners(topLeft: Int, topRight: Int, bottomRight: Int, bottomLeft: Int): RectDraw {
		val f1 = dp(topLeft).toFloat()
		val f2 = dp(topRight).toFloat()
		val f3 = dp(bottomRight).toFloat()
		val f4 = dp(bottomLeft).toFloat()

		val arr = floatArrayOf(f1, f1, f2, f2, f3, f3, f4, f4)
		value.cornerRadii = arr
		return this
	}

	fun stroke(width: Int, color: Int): RectDraw {
		value.setStroke(dp(width), color)
		return this
	}

	fun size(w: Int, h: Int = w): RectDraw {
		value.setSize(dp(w), dp(h))
		return this
	}
}