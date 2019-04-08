package yet.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import dev.entao.yapp.App
import yet.theme.Colors
import yet.util.Task

/**
 * Created by yet on 2015/10/31.
 */
class MyProgressBarIndeterminate(context: Context) : View(context) {
	private val MIN_WIDTH = App.dp2px(30)//
	private val durationIndeterm = 5000//
	private var foreImg: Drawable? = null
	private var indeterTime = 0

	fun runMe() {
		if (isVisible) {
			if (indeterTime < 0) {
				--indeterTime
			} else {
				++indeterTime
			}
			postInvalidate()
			Task.foreDelay(INTERVAL_INDETERM.toLong()) { runMe() }
		}
	}


	val isVisible: Boolean
		get() = visibility == View.VISIBLE

	init {
		setBackDrawable(makeDrawable(0, 0.5f, Color.parseColor("#999999"), Color.parseColor("#555555"), Color.parseColor("#777777")))
		setForeDrawable(makeDrawable(0, 0.5f, Colors.Safe, Color.rgb(0, 255, 0), Colors.Safe))
	}

	override fun onVisibilityChanged(changedView: View, visibility: Int) {
		super.onVisibilityChanged(changedView, visibility)
		startBlockAnim()
	}

	private fun startBlockAnim() {
		if (isVisible) {
			Task.fore { runMe() }
		}
	}

	override fun onDraw(canvas: Canvas) {
		if (isVisible) {
			val width = width
			var w = width / 5
			if (w < MIN_WIDTH) {
				w = width / 2
			}
			var times = durationIndeterm / INTERVAL_INDETERM
			if (times < 1) {
				times = 1
			}
			var inc = width / times
			if (inc < 1) {
				inc = 1
			}
			var left = 0
			if (indeterTime >= 0) {
				left = indeterTime * inc
				if (left + w > width) {
					left = width - w
					indeterTime = -1
				}
			} else {
				left = width - w + indeterTime * inc
				if (left < 0) {
					left = 0
					indeterTime = 0
				}
			}
			foreImg!!.setBounds(left, 0, left + w, height)
			foreImg!!.draw(canvas)
		}

	}

	@Suppress("DEPRECATION")
	fun setBackDrawable(drawable: Drawable): MyProgressBarIndeterminate {
		setBackgroundDrawable(drawable)
		return this
	}

	fun setForeDrawable(drawable: Drawable): MyProgressBarIndeterminate {
		foreImg = drawable
		postInvalidate()
		return this
	}

	fun show(): MyProgressBarIndeterminate {
		visibility = View.VISIBLE
		return this
	}

	fun hide() {
		visibility = View.GONE
	}

	companion object {
		private val INTERVAL_INDETERM = 30//动画, 20毫秒刷新一次, 无限

		fun makeDrawable(cornerRadius: Int, color: Int): GradientDrawable {
			val gd = GradientDrawable()
			gd.cornerRadius = cornerRadius.toFloat()
			gd.setColor(color)
			return gd
		}

		fun makeDrawable(cornerRadius: Int, centerY: Float, startColor: Int, centerColor: Int, endColor: Int): GradientDrawable {
			val gd = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(startColor, centerColor, endColor))
			gd.setGradientCenter(0f, centerY)
			gd.shape = GradientDrawable.RECTANGLE
			gd.cornerRadius = cornerRadius.toFloat()
			return gd
		}
	}
}
