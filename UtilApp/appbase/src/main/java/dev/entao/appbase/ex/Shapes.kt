package dev.entao.appbase.ex

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import dev.entao.appbase.ex.Colors
import dev.entao.appbase.ex.dp
import dev.entao.appbase.ex.px2dp

/**
 *
 */
object Shapes {
	open class LineOption {
		//px
		var strokeWidthPx: Int = 0
		var strokeColor: Int = Colors.BLACK

		var dashWidthPx: Int = 0
		var dashGapPx: Int = 0

	}

	open class RectOption : LineOption() {
		//null则不填充
		var fillColor: Int? = null
		//0则是直角, px
		var cornerPx: Int = 0

		var widthPx: Int = 0
		var heightPx: Int = 0

		//4个角, 左上, 右上, 右下, 左下
		// 8个值, 单位px
		var cornerListPx: List<Int> = emptyList()

		var cornerDp: Int
			get() = px2dp(cornerPx)
			set(value) {
				cornerPx = dp(value)
			}

		fun size(wDp: Int, hDp: Int = wDp) {
			widthPx = dp(wDp)
			heightPx = dp(hDp)
		}

		fun cornerListPx(leftTop: Int, rightTop: Int, rightBottom: Int, leftBottom: Int) {
			cornerListPx = listOf(leftTop, leftTop, rightTop, rightTop,
					rightBottom, rightBottom, leftBottom, leftBottom)
		}

		fun cornerListDp(leftTop: Int, rightTop: Int, rightBottom: Int, leftBottom: Int) {
			cornerListPx = listOf(dp(leftTop), dp(leftTop), dp(rightTop), dp(rightTop),
					dp(rightBottom), dp(rightBottom), dp(leftBottom), dp(leftBottom))
		}
	}

	class OvalOption : RectOption()

	fun line(strokeWidthDp: Int, strokeColor: Int): GradientDrawable {
		return line {
			this.strokeWidthPx = dp(strokeWidthDp)
			this.strokeColor = strokeColor
		}
	}

	fun line(block: LineOption.() -> Unit): GradientDrawable {
		val lo = LineOption()
		lo.block()
		val gd = GradientDrawable()
		gd.shape = GradientDrawable.LINE
		gd.setStroke(lo.strokeWidthPx, lo.strokeColor)
		return gd
	}


	fun rect(block: RectOption.() -> Unit): Drawable {
		val ro = RectOption()
		ro.block()
		val gd = GradientDrawable()
		gd.shape = GradientDrawable.RECTANGLE
		if (ro.cornerListPx.isNotEmpty()) {
			val farr = FloatArray(8)
			for (n in 0..7) {
				val v = ro.cornerListPx[n]
				farr[n] = v.toFloat()
			}
			gd.cornerRadii = farr
		} else if (ro.cornerPx > 0) {
			gd.cornerRadius = ro.cornerPx.toFloat()
		}
		if (ro.fillColor != null) {
			gd.setColor(ro.fillColor!!)
		}
		if (ro.strokeWidthPx > 0) {
			gd.setStroke(ro.strokeWidthPx, ro.strokeColor, ro.dashWidthPx.toFloat(), ro.dashGapPx.toFloat())
		}
		if (ro.widthPx > 0 && ro.heightPx > 0) {
			gd.setSize(ro.widthPx, ro.heightPx)
		}
		return gd
	}

	fun oval(block: OvalOption.() -> Unit): GradientDrawable {
		val oo = OvalOption()
		oo.block()
		val drawable = GradientDrawable()
		drawable.shape = GradientDrawable.OVAL
		if (oo.fillColor != null) {
			drawable.setColor(oo.fillColor!!)
		}
		if (oo.strokeWidthPx > 0) {
			drawable.setStroke(oo.strokeWidthPx, oo.strokeColor, oo.dashWidthPx.toFloat(), oo.dashGapPx.toFloat())
		}
		if (oo.widthPx > 0 && oo.heightPx > 0) {
			drawable.setSize(oo.widthPx, oo.heightPx)
		}
		return drawable
	}


}
