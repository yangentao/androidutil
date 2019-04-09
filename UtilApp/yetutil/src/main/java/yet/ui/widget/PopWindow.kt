package yet.ui.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.StyleRes
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import dev.entao.appbase.ex.Colors
import dev.entao.appbase.ex.dp
import yet.ui.ext.backColor
import yet.ui.ext.padding
import yet.ui.ext.textColorWhite
import yet.ui.viewcreator.createTextView

/**
 * Created by entaoyang@163.com on 2016-11-20.
 */

open class PopWindow {
	var pop: PopupWindow? = null
	var activity: Activity? = null
	var oldAlpha = 1f
	var gravity = Gravity.CENTER
	var x = 0
	var y = 0
	var w = ViewGroup.LayoutParams.MATCH_PARENT
	var h = ViewGroup.LayoutParams.WRAP_CONTENT
	var animStyle = 0
	var autoDarkActivity = false

	var onDismiss: () -> Unit = {}
	var onCreateView: (context: Context) -> View = {
		val tv = it.createTextView().padding(20).backColor(Colors.GreenMajor).textColorWhite()
		tv.setText("Hello PopWindow")
		tv
	}

	fun animStyle(@StyleRes style: Int): PopWindow {
		this.animStyle = style
		return this
	}

	fun widthFill(): PopWindow {
		w = ViewGroup.LayoutParams.MATCH_PARENT
		return this
	}

	fun widthWrap(): PopWindow {
		w = ViewGroup.LayoutParams.WRAP_CONTENT
		return this
	}

	fun width(dpWidth: Int): PopWindow {
		w = dp(dpWidth)
		return this
	}

	fun heightFill(): PopWindow {
		h = ViewGroup.LayoutParams.MATCH_PARENT
		return this
	}

	fun heightWrap(): PopWindow {
		h = ViewGroup.LayoutParams.WRAP_CONTENT
		return this
	}

	fun height(dpHeight: Int): PopWindow {
		h = dp(dpHeight)
		return this
	}

	protected fun createPop(): PopupWindow {
		val p = PopupWindow(activity)
		pop = p
		p.animationStyle = this.animStyle
		p.width = w
		p.height = h
		p.isFocusable = true
		p.isTouchable = true
		p.isOutsideTouchable = true
		p.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

		p.setOnDismissListener {
			dark(oldAlpha)
			this@PopWindow.onDismiss()
		}

		onCreatePopWindow(p)
		return p
	}


	fun autoDark(dark: Boolean): PopWindow {
		this.autoDarkActivity = dark
		return this
	}

	fun autoDark(): PopWindow {
		this.autoDarkActivity = true
		return this
	}

	fun gravity(g: Int): PopWindow {
		this.gravity = g
		return this
	}

	fun gravityNone(): PopWindow {
		return gravity(Gravity.NO_GRAVITY)
	}

	fun gravityTop(): PopWindow {
		return gravity(Gravity.TOP)
	}

	fun gravityTopLeft(): PopWindow {
		return gravity(Gravity.TOP or Gravity.LEFT)
	}

	fun gravityTopRight(): PopWindow {
		return gravity(Gravity.TOP or Gravity.RIGHT)
	}

	fun gravityTopCenter(): PopWindow {
		return gravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
	}

	fun gravityBottom(): PopWindow {
		return gravity(Gravity.BOTTOM)
	}

	fun gravityBottomLeft(): PopWindow {
		return gravity(Gravity.BOTTOM or Gravity.LEFT)
	}

	fun gravityBottompRight(): PopWindow {
		return gravity(Gravity.BOTTOM or Gravity.RIGHT)
	}

	fun gravityBottomCenter(): PopWindow {
		return gravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
	}

	fun gravityCenter(): PopWindow {
		return gravity(Gravity.CENTER)
	}

	fun gravityCenterLeft(): PopWindow {
		return gravity(Gravity.CENTER_VERTICAL or Gravity.LEFT)
	}

	fun gravityCenterRight(): PopWindow {
		return gravity(Gravity.CENTER_VERTICAL or Gravity.RIGHT)
	}

	fun location(xDp: Int, yDp: Int): PopWindow {
		this.x = dp(xDp)
		this.y = dp(yDp)
		return this
	}

	fun show(activity: Activity, parent: View) {
		this.activity = activity
		oldAlpha = activity.window.attributes.alpha
		dark(0.5f)
		val p = createPop()
		p.contentView = onCreateView(activity)
		p.showAtLocation(parent, gravity, x, y)
	}

	open fun onCreatePopWindow(p: PopupWindow) {

	}


	fun dismiss() {
		if (pop != null) {
			pop!!.dismiss()
		}
	}

	protected fun dark(f: Float) {
		if (activity != null && autoDarkActivity) {
			val p = activity!!.window.attributes
			p.alpha = f
			activity!!.window.attributes = p
		}
	}

}
