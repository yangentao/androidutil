package yet.ui.ext

import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import dev.entao.appbase.ex.Bmp
import dev.entao.appbase.ex.Colors
import dev.entao.appbase.ex.drawable

/**
 * Created by entaoyang@163.com on 16/3/12.
 */

class Divider(var color: Int = Colors.LineGray) {
	//px
	var size: Int = 1
	var begin: Boolean = false
	var mid: Boolean = true
	var end: Boolean = false
	var pad: Int = 0

	fun size(n: Int): Divider {
		this.size = n
		return this
	}

	fun color(color: Int): Divider {
		this.color = color
		return this
	}

	fun begin(b: Boolean = true): Divider {
		this.begin = b
		return this
	}

	fun mid(b: Boolean = true): Divider {
		this.mid = b
		return this
	}

	fun end(b: Boolean = true): Divider {
		this.end = b
		return this
	}

	fun pad(n: Int): Divider {
		this.pad = n
		return this
	}

}

fun <T : LinearLayout> T.divider(): T {
	return this.divider(Divider(Colors.LineGray))
}

fun <T : LinearLayout> T.divider(color: Int): T {
	return this.divider(Divider(color))
}

fun <T : LinearLayout> T.divider(block: Divider.() -> Unit): T {
	val ld = Divider()
	ld.block()
	return this.divider(ld)
}

fun <T : LinearLayout> T.divider(ld: Divider): T {
	if (ld.size > 0) {
		val d = Bmp.line(ld.size, ld.size, ld.color).drawable
		this.dividerDrawable = d
		this.dividerPadding = dp(ld.pad)
		var n = LinearLayout.SHOW_DIVIDER_NONE
		if (ld.begin) {
			n = n or LinearLayout.SHOW_DIVIDER_BEGINNING
		}
		if (ld.mid) {
			n = n or LinearLayout.SHOW_DIVIDER_MIDDLE
		}
		if (ld.end) {
			n = n or LinearLayout.SHOW_DIVIDER_END
		}
		this.showDividers = n
	}
	return this
}

fun <T : LinearLayout> T.hideDivider(): T {
	this.showDividers = LinearLayout.SHOW_DIVIDER_NONE
	return this
}


fun <T : LinearLayout> T.orientationVertical(): T {
	this.orientation = LinearLayout.VERTICAL
	return this
}

fun <T : LinearLayout> T.orientationHorizontal(): T {
	this.orientation = LinearLayout.HORIZONTAL
	return this
}

fun <T : LinearLayout> T.horizontal(): T {
	this.orientation = LinearLayout.HORIZONTAL
	return this
}

fun <T : LinearLayout> T.vertical(): T {
	this.orientation = LinearLayout.VERTICAL
	return this
}

fun <T : LinearLayout> T.isVertical(): Boolean {
	return this.orientation == LinearLayout.VERTICAL
}

fun <T : LinearLayout> T.gravity(n: Int): T {
	this.setGravity(n)
	return this
}

fun <T : LinearLayout> T.gravityCenterVertical(): T {
	this.setGravity(Gravity.CENTER_VERTICAL)
	return this
}

fun <T : LinearLayout> T.gravityCenterHorizontal(): T {
	this.setGravity(Gravity.CENTER_HORIZONTAL)
	return this
}

fun <T : LinearLayout> T.gravityLeftCenter(): T {
	this.setGravity(Gravity.LEFT or Gravity.CENTER)
	return this
}

fun <T : LinearLayout> T.gravityRightCenter(): T {
	this.setGravity(Gravity.RIGHT or Gravity.CENTER)
	return this
}

fun <T : LinearLayout> T.gravityCenter(): T {
	this.setGravity(Gravity.CENTER)
	return this
}

fun <T : LinearLayout> T.addViewParam(view: View, f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.addView(view, lp)
	return this
}

fun <T : LinearLayout> T.addViewParam(view: View, index: Int, f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.addView(view, index, lp)
	return this
}

fun <T : LinearLayout> T.addView(view: View, f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.addView(view, lp)
	return this
}

fun <T : LinearLayout> T.addView(view: View, index: Int, f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.addView(view, index, lp)
	return this
}

fun <T : LinearLayout> T.addGrayLine(size: Int = 1, margin: Int = 0, color: Int = Colors.LineGray): View {
	return addGrayLine {
		this.size = size
		this.marginLeft = margin
		this.marginRight = margin
		this.color = color
	}
}

fun <T : LinearLayout> T.addGrayLine(config: GrayLineConfig): View {
	val view = View(context).genId().backColor(config.color)
	if (this.isVertical()) {
		addViewParam(view) {
			widthFill().height(config.size).margins(config.marginLeft, config.marginTop, config.marginRight, config.marginBottom)
		}
	} else {
		addViewParam(view) {
			heightFill().width(config.size).margins(config.marginLeft, config.marginTop, config.marginRight, config.marginBottom)
		}
	}
	return view
}

fun <T : LinearLayout> T.addGrayLine(block: GrayLineConfig.() -> Unit): View {
	val config = GrayLineConfig()
	config.block()
	return addGrayLine(config)
}


fun LinearLayout.addFlex(weight: Double = 1.0): View {
	val view = View(this.context).genId().invisiable()
	if (this.isVertical()) {
		this.addViewParam(view) {
			widthFill().height(0).weight(weight)
		}
	} else {
		this.addViewParam(view) {
			heightFill().width(0).weight(weight)
		}
	}
	return view
}
