package yet.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import yet.theme.Colors
import yet.util.Task

/**
 * Created by yet on 2015/10/31.
 */
class XProgressBar(context: Context) : View(context) {
	private var duration = 200//动画时间 200毫秒---从0到最大值(100%)的时间
	private var foreImg: Drawable? = null
	private var max = 100
	private var progress = 0
	private var animProgress = 0

	val isVisible: Boolean
		get() = visibility == View.VISIBLE

	fun runMe() {//每次递进1%, 或1
		val times = duration / INTERVAL_A
		if (times < 1) {
			animProgress = progress
			postInvalidate()
		} else {
			var percent = max / times
			if (percent == 0) {
				percent = 2
			}
			val cha = progress - animProgress
			if (Math.abs(cha) <= percent) {
				animProgress = progress
				postInvalidate()
			} else {
				if (cha > 0) {
					animProgress += percent
				} else {
					animProgress -= percent
				}
				postInvalidate()
				Task.foreDelay(INTERVAL_A.toLong()) {
					runMe()
				}
			}
		}
	}

	init {
		setBackDrawable(makeDrawable(0, 0.5f, Color.parseColor("#999999"), Color.parseColor("#555555"), Color.parseColor("#777777")))
		setForeDrawable(makeDrawable(0, 0.5f, Colors.Safe, Color.rgb(0, 255, 0), Colors.Safe))
	}

	override fun onDraw(canvas: Canvas) {
		var prog = progress
		if (animProgress >= 0) {
			prog = animProgress
		}
		val width = width
		max = if (max == 0) 1 else max
		var w = prog * width / max
		w = if (w < 0) -w else w
		foreImg!!.setBounds(0, 0, w, height)
		foreImg!!.draw(canvas)

	}

	fun setDuration(duration: Int): XProgressBar {
		this.duration = duration
		return this
	}

	fun setBackDrawable(drawable: Drawable): XProgressBar {
		setBackgroundDrawable(drawable)
		return this
	}

	fun setForeDrawable(drawable: Drawable): XProgressBar {
		foreImg = drawable
		postInvalidate()
		return this
	}

	fun setProgress(p: Int): XProgressBar {
		var progress = p
		if (progress < 0) {
			progress = 0
		}
		if (progress > max) {
			progress = max
		}
		this.animProgress = this.progress
		this.progress = progress
		Task.fore { this.runMe() }
		return this
	}

	fun postProgress(progress: Int) {
		Task.fore{ setProgress(progress) }
	}

	fun setMax(max: Int): XProgressBar {
		var maxVal = max
		if (maxVal < 0) {
			maxVal = 1
		}
		this.max = maxVal
		postInvalidate()
		return this
	}

	fun postMax(max: Int) {
		Task.fore{ setMax(max) }
	}

	fun show(): XProgressBar {
		visibility = View.VISIBLE
		return this
	}

	fun show(max: Int): XProgressBar {
		visibility = View.VISIBLE
		this.progress = 0
		this.animProgress = 0
		setMax(max)
		return this
	}

	fun postShow(max: Int) {
		Task.fore{ show(max) }
	}

	fun postHide() {
		Task.fore{ hide() }
	}

	fun hide() {
		visibility = View.GONE
	}

	companion object {
		private val INTERVAL_A = 20//动画, 20毫秒刷新一次 , 有限

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
