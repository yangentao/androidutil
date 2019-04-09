package dev.entao.ui.ext

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout

/**
 * Created by entaoyang@163.com on 2016-07-21.
 */

fun <T : LinearLayout.LayoutParams> T.set(view: View) {
	view.layoutParams = this
}

val LParam: LinearLayout.LayoutParams
	get() {
		return lParam()
	}


fun lParam(): LinearLayout.LayoutParams {
	return linearParam()
}

fun linearParam(): LinearLayout.LayoutParams {
	return LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
}

fun linearParam(f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): LinearLayout.LayoutParams {
	var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
	lp.f()
	return lp
}


val <T : LinearLayout.LayoutParams> T.FlexHor: T
	get() {
		return this.WidthFlex
	}
val <T : LinearLayout.LayoutParams> T.WidthFlex: T
	get() {
		return this.width(0).weight(1)
	}

val <T : LinearLayout.LayoutParams> T.FlexVer: T
	get() {
		return this.HeightFlex
	}
val <T : LinearLayout.LayoutParams> T.HeightFlex: T
	get() {
		return this.height(0).weight(1)
	}

fun <T : LinearLayout.LayoutParams> T.weight_(w: Int): T {
	return weight(w.toFloat())
}

fun <T : LinearLayout.LayoutParams> T.weight(w: Int): T {
	return weight(w.toFloat())
}

fun <T : LinearLayout.LayoutParams> T.weight(w: Double): T {
	return weight(w.toFloat())
}

fun <T : LinearLayout.LayoutParams> T.weight(w: Float): T {
	weight = w
	return this
}


val <T : LinearLayout.LayoutParams> T.GravityTop: T
	get() {
		gravity = Gravity.TOP
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityTopCenter(): T {
	gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityTopCenter: T
	get() {
		gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
		return this
	}


val <T : LinearLayout.LayoutParams> T.GravityBottom: T
	get() {
		gravity = Gravity.BOTTOM
		return this
	}


fun <T : LinearLayout.LayoutParams> T.gravityBottomCenter(): T {
	gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityBottomCenter: T
	get() {
		gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
		return this
	}


val <T : LinearLayout.LayoutParams> T.GravityLeft: T
	get() {
		gravity = Gravity.LEFT
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityLeftCenter(): T {
	gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityLeftCenter: T
	get() {
		gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
		return this
	}


val <T : LinearLayout.LayoutParams> T.GravityRight: T
	get() {
		gravity = Gravity.RIGHT
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityRightCenter(): T {
	gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityRightCenter: T
	get() {
		gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityFill(): T {
	gravity = Gravity.FILL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityFill: T
	get() {
		gravity = Gravity.FILL
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityFillVertical(): T {
	gravity = Gravity.FILL_VERTICAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityFillVertical: T
	get() {
		gravity = Gravity.FILL_VERTICAL
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityFillHorizontal(): T {
	gravity = Gravity.FILL_HORIZONTAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityFillHorizontal: T
	get() {
		gravity = Gravity.FILL_HORIZONTAL
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityCenterVertical(): T {
	gravity = Gravity.CENTER_VERTICAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityCenterVertical: T
	get() {
		gravity = Gravity.CENTER_VERTICAL
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityCenterHorizontal(): T {
	gravity = Gravity.CENTER_HORIZONTAL
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityCenterHorizontal: T
	get() {
		gravity = Gravity.CENTER_HORIZONTAL
		return this
	}

fun <T : LinearLayout.LayoutParams> T.gravityCenter(): T {
	gravity = Gravity.CENTER
	return this
}

val <T : LinearLayout.LayoutParams> T.GravityCenter: T
	get() {
		gravity = Gravity.CENTER
		return this
	}
