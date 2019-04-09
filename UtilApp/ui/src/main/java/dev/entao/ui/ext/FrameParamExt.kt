package dev.entao.ui.ext

import android.view.Gravity
import android.widget.FrameLayout

/**
 * Created by entaoyang@163.com on 2016-10-29.
 */


val FParam: FrameLayout.LayoutParams
	get() {
		return FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
	}


val <T : FrameLayout.LayoutParams> T.GravityTop: T
	get() {
		gravity = Gravity.TOP
		return this
	}


val <T : FrameLayout.LayoutParams> T.GravityTopCenter: T
	get() {
		gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityTopLeft: T
	get() {
		gravity = Gravity.TOP or Gravity.LEFT
		return this
	}
val <T : FrameLayout.LayoutParams> T.GravityTopRight: T
	get() {
		gravity = Gravity.TOP or Gravity.RIGHT
		return this
	}
val <T : FrameLayout.LayoutParams> T.GravityBottom: T
	get() {
		gravity = Gravity.BOTTOM
		return this
	}


val <T : FrameLayout.LayoutParams> T.GravityBottomCenter: T
	get() {
		gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
		return this
	}


val <T : FrameLayout.LayoutParams> T.GravityBottomLeft: T
	get() {
		gravity = Gravity.BOTTOM or Gravity.LEFT
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityBottomRight: T
	get() {
		gravity = Gravity.BOTTOM or Gravity.RIGHT
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityLeft: T
	get() {
		gravity = Gravity.LEFT
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityLeftCenter: T
	get() {
		gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityRight: T
	get() {
		gravity = Gravity.RIGHT
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityRightCenter: T
	get() {
		gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityFill: T
	get() {
		gravity = Gravity.FILL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityFillVertical: T
	get() {
		gravity = Gravity.FILL_VERTICAL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityFillHorizontal: T
	get() {
		gravity = Gravity.FILL_HORIZONTAL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityCenterVertical: T
	get() {
		gravity = Gravity.CENTER_VERTICAL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityCenterHorizontal: T
	get() {
		gravity = Gravity.CENTER_HORIZONTAL
		return this
	}

val <T : FrameLayout.LayoutParams> T.GravityCenter: T
	get() {
		gravity = Gravity.CENTER
		return this
	}


fun <T : FrameLayout.LayoutParams> T.gravity(g: Int): T {
	gravity = g
	return this
}
