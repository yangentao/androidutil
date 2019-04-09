package dev.entao.appbase.ex
import android.content.res.ColorStateList
import android.util.Log

/**
 * Created by entaoyang@163.com on 2016-10-31.
 */


class ColorList(val normal: Int) {
	private val colors = IntArray(10)
	private val states = arrayOfNulls<IntArray>(10)
	private var index = 0

	val value: ColorStateList get() {
		return this.get()
	}

	fun get(): ColorStateList {
		addColor(normal)
		return make()!!
	}

	private fun addColor(color: Int?, vararg states: Int) {
		if (color != null) {
			if (index >= 10) {
				Log.e("colorlist","max color num is 10")
				return
			}
			colors[index] = color
			this.states[index] = states
			++index
		}
	}

	private fun make(): ColorStateList? {
		if (index <= 0) {
			return null
		}
		val a = Array<IntArray>(index) {
			states[it]!!
		}
		val b = IntArray(index) {
			colors[it]
		}
		return ColorStateList(a, b)
	}


	fun selected(c: Int, selected: Boolean = true): ColorList {
		addColor(c, if (selected) android.R.attr.state_selected else -android.R.attr.state_selected)
		return this
	}

	fun pressed(c: Int, pressed: Boolean = true): ColorList {
		addColor(c, if (pressed) android.R.attr.state_pressed else -android.R.attr.state_pressed)
		return this
	}

	fun disabled(c: Int): ColorList {
		return enabled(c, false)
	}

	fun enabled(c: Int, enabled: Boolean = true): ColorList {
		addColor(c, if (enabled) android.R.attr.state_enabled else -android.R.attr.state_enabled)
		return this
	}

	fun checked(c: Int, checked: Boolean = true): ColorList {
		addColor(c, if (checked) android.R.attr.state_checked else -android.R.attr.state_checked)
		return this
	}

	fun focused(c: Int, focused: Boolean = true): ColorList {
		addColor(c, if (focused) android.R.attr.state_focused else -android.R.attr.state_focused)
		return this
	}


}
