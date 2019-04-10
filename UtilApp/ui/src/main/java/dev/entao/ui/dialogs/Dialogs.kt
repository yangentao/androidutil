@file:Suppress("unused")

package dev.entao.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.view.Gravity
import dev.entao.appbase.ex.dp
import dev.entao.ui.ext.act
import dev.entao.ui.ext.inputTypeNumber
import dev.entao.ui.ext.textS
import dev.entao.ui.page.BaseFragment

/**
 * Created by entaoyang@163.com on 16/5/10.
 */


fun Dialog.gravityCenter(): Dialog {
	val lp = this.window.attributes
	lp.gravity = Gravity.CENTER
	return this
}

fun Dialog.gravityTop(margin: Int = 0): Dialog {
	val lp = this.window.attributes
	lp.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
	lp.y = dp(margin)
	return this
}

fun Dialog.gravityBottom(margin: Int = 0): Dialog {
	val lp = this.window.attributes
	lp.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
	lp.y = dp(margin)
	return this
}


fun BaseFragment.confirm(title: String?, msg: String, block: () -> Unit) {
	DialogX.confirm(act, msg, title, block)
}


fun BaseFragment.showInput(title: String, defaultValue: String = "", block: (String) -> Unit) {
	DialogX.input(act, title, defaultValue, block)
}

fun BaseFragment.showInputInteger(title: String, n: Long = 0, block: (Long) -> Unit) {
	val d = DialogX(act)
	d.title(title)
	d.bodyInput {
		this.textS = n.toString()
		this.inputTypeNumber()
	}
	d.cancel()
	d.ok {
		val s = d.result.first() as String
		if (s.trim().isNotEmpty()) {
			val lval = s.trim().toLongOrNull()
			if (lval != null) {
				block(lval)
			}
		}
	}
	d.show()
}

fun BaseFragment.showInputMultiLine(title: String, defaultValue: String = "", block: (String) -> Unit) {
	DialogX.inputLines(act, title, defaultValue, block)
}

fun BaseFragment.alert(msg: String, title: String? = null, dismissBlock: () -> Unit = {}) {
	if (activity != null) {
		DialogX.alert(act, msg, title, dismissBlock)
	} else {
		toast(msg)
	}
}

fun Activity.alert(msg: String, title: String? = null, dismissBlock: () -> Unit = {}) {
	DialogX.alert(this, msg, title, dismissBlock)
}
