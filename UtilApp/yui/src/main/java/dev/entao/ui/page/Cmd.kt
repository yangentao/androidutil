package dev.entao.ui.page

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dev.entao.ui.ext.HeightWrap
import dev.entao.ui.ext.LParam
import dev.entao.ui.ext.WidthFill
import dev.entao.ui.ext.textS
import dev.entao.ui.widget.ActionMenuItemInfo

class Cmd(var cmd: String = genCmd) {

	init {
		if (cmd.isEmpty()) {
			cmd = genCmd
		}
	}

	lateinit var view: View
	var param: ViewGroup.MarginLayoutParams = LParam.WidthFill.HeightWrap

	val items = ArrayList<ActionMenuItemInfo>()

	var clickable = true
	var onClick: (Cmd) -> Unit = {}


	fun setText(text: String) {
		(view as TextView).textS = text
	}

	fun setImage(resId: Int) {
		(view as ImageView).setImageResource(resId)
	}

	fun menu(block: Cmd.() -> Unit) {
		this.block()
	}

	fun menuItem(text: String): ActionMenuItemInfo {
		return menuItem(text) {}
	}

	fun menuItem(text: String, resId: Int): ActionMenuItemInfo {
		return menuItem(text) {
			this.resIcon = resId
		}
	}

	fun menuItem(text: String, block: ActionMenuItemInfo.() -> Unit): ActionMenuItemInfo {
		val info = ActionMenuItemInfo(text)
		info.block()
		return menuItem(info)
	}

	fun menuItem(item: ActionMenuItemInfo): ActionMenuItemInfo {
		if (item.index < 0) {
			items.add(item)
		} else {
			items.add(item.index, item)
		}
		return item
	}

	companion object {
		const val Sep = "_sep_"
		private var idN: Int = 0

		val genCmd: String
			get() {
				++idN
				return "Cmd_$idN"
			}


	}
}