package dev.entao.appbase.ex

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.annotation.DrawableRes
import dev.entao.appbase.App

/**
 * Created by entaoyang@163.com on 2016-10-31.
 */

class ImageStated(var normal: Drawable? = null) {
	private var stateDrawable = StateListDrawable()

	constructor(@DrawableRes resId: Int) : this(App.drawable(resId))

	//如果d是null, 不执行任何动作, 忽略掉
	private fun addDrawable(d: Drawable?, vararg states: Int) {
		if (d != null) {
			stateDrawable.addState(states, d)
		}
	}

	private fun addColorDrawable(color: Int?, vararg states: Int) {
		if (color != null) {
			stateDrawable.addState(states, ColorDrawable(color))
		}
	}

	val value: StateListDrawable get() {
		return this.get()
	}

	fun get(): StateListDrawable {
		addDrawable(normal)
		return stateDrawable
	}

	fun normalColor(color: Int): ImageStated {
		return normal(ColorDrawable(color))
	}

	fun normal(@DrawableRes id: Int): ImageStated {
		return normal(App.drawable(id))
	}

	fun normal(d: Drawable): ImageStated {
		normal = d
		return this
	}


	fun selectedColor(color: Int, selected: Boolean = true): ImageStated {
		return selected(ColorDrawable(color), selected)
	}

	fun selected(@DrawableRes id: Int, selected: Boolean = true): ImageStated {
		return selected(App.drawable(id), selected)
	}

	fun selected(d: Drawable?, selected: Boolean = true): ImageStated {
		if (d == null) {
			return this
		}
		addDrawable(d, if (selected) android.R.attr.state_selected else -android.R.attr.state_selected)
		return this
	}


	fun pressedColor(color: Int, pressed: Boolean = true): ImageStated {
		return pressed(ColorDrawable(color), pressed)
	}

	fun pressed(@DrawableRes id: Int, pressed: Boolean = true): ImageStated {
		return pressed(App.drawable(id), pressed)
	}

	fun pressed(d: Drawable?, pressed: Boolean = true): ImageStated {
		if (d == null) {
			return this
		}
		addDrawable(d, if (pressed) android.R.attr.state_pressed else -android.R.attr.state_pressed)
		return this
	}


	fun disabledColor(color: Int): ImageStated {
		return enabledColor(color, false)
	}

	fun disabled(@DrawableRes id: Int): ImageStated {
		return disabled(App.drawable(id))
	}

	fun disabled(d: Drawable?): ImageStated {
		return enabled(d, false)
	}

	fun enabledColor(color: Int, enabled: Boolean = true): ImageStated {
		return enabled(ColorDrawable(color), enabled)
	}

	fun enabled(@DrawableRes id: Int, enabled: Boolean = true): ImageStated {
		return enabled(App.drawable(id), enabled)
	}

	fun enabled(d: Drawable?, enabled: Boolean = true): ImageStated {
		if (d == null) {
			return this
		}
		addDrawable(d, if (enabled) android.R.attr.state_enabled else -android.R.attr.state_enabled)
		return this
	}


	fun checkedColor(color: Int, checked: Boolean = true): ImageStated {
		return checked(ColorDrawable(color), checked)
	}

	fun checked(@DrawableRes id: Int, checked: Boolean = true): ImageStated {
		return checked(App.drawable(id), checked)
	}

	fun checked(d: Drawable?, checked: Boolean = true): ImageStated {
		if (d == null) {
			return this
		}
		addDrawable(d, if (checked) android.R.attr.state_checked else -android.R.attr.state_checked)
		return this
	}


	fun focusedColor(color: Int, focused: Boolean = true): ImageStated {
		return focused(ColorDrawable(color), focused)
	}

	fun focused(@DrawableRes id: Int, focused: Boolean = true): ImageStated {
		return focused(App.drawable(id), focused)
	}

	fun focused(d: Drawable?, focused: Boolean = true): ImageStated {
		if (d == null) {
			return this
		}
		addDrawable(d, if (focused) android.R.attr.state_focused else -android.R.attr.state_focused)
		return this
	}


}