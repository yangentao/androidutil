package dev.entao.appbase.ex

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable

/**
 * Created by entaoyang@163.com on 2016-10-31.
 */

class ColorStated(val normal: Int) {
	private var stateDrawable = StateListDrawable()

	private fun addColor(color: Int, vararg states: Int): ColorStated {
		stateDrawable.addState(states, ColorDrawable(color))
		return this
	}

	val value: StateListDrawable get() {
		return this.get()
	}

	fun get(): StateListDrawable {
		addColor(normal)
		return stateDrawable
	}

	fun selected(color: Int, selected: Boolean = true): ColorStated {
		return addColor(color, if (selected) android.R.attr.state_selected else -android.R.attr.state_selected)
	}

	fun pressed(color: Int, pressed: Boolean = true): ColorStated {
		return addColor(color, if (pressed) android.R.attr.state_pressed else -android.R.attr.state_pressed)
	}

	fun enabled(color: Int, enabled: Boolean = true): ColorStated {
		return addColor(color, if (enabled) android.R.attr.state_enabled else -android.R.attr.state_enabled)
	}

	fun disabled(color: Int): ColorStated {
		return enabled(color, false)
	}

	fun checked(color: Int, checked: Boolean = true): ColorStated {
		return addColor(color, if (checked) android.R.attr.state_checked else -android.R.attr.state_checked)
	}

	fun focused(color: Int, focused: Boolean = true): ColorStated {
		return addColor(color, if (focused) android.R.attr.state_focused else -android.R.attr.state_focused)
	}

}