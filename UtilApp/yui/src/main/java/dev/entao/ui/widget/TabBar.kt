package dev.entao.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import dev.entao.appbase.ex.Colors
import dev.entao.ui.ext.*

/**
 * Created by entaoyang@163.com on 2018-04-18.
 */

class TabBar(context: Context) : LinearLayout(context) {
	val itemList = ArrayList<TabBarItemView>()
	var autoTintDrawable = true

	var pushModel: Boolean = false
		private set

	private var itemBack = ArrayList<TabBarItemView>()

	var onUnselect: (TabBarItemView) -> Unit = {

	}
	var onReselect: (TabBarItemView) -> Unit = {
	}
	var onSelect: (TabBarItemView) -> Unit = {
	}

	init {
		genId()
		horizontal()
		backColor(Colors.WHITE)
		this.layoutParams = Param.WidthFill.HeightBar
	}

	fun push(block: TabBar.() -> Unit) {
		if (!pushModel) {
			pushModel = true
			moveTo(itemList, itemBack)
			this.block()
			commit()
		}
	}

	fun pop() {
		if (pushModel) {
			pushModel = false
			moveTo(itemBack, itemList)
			commit()
		}
	}

	private fun <T> moveTo(from: ArrayList<T>, dest: ArrayList<T>) {
		dest.clear()
		dest.addAll(from)
		from.clear()
	}


	fun find(block: (TabBarItemView) -> Boolean): TabBarItemView? {
		return itemList.find(block)
	}

	fun find(text: String): TabBarItemView? {
		return find {
			it.textView.textS == text
		}
	}

	fun commit() {
		removeAllViews()
		for (v in itemList) {
			this.addView(v, LParam.WidthFlex.HeightFill)
			v.onClick { clickTab(it, true) }
		}
		if (selectedItem == null) {
			val v = itemList.firstOrNull()
			if (v != null) {
				clickTab(v, true)
			}
		}
	}

	val selectedItem: TabBarItemView?
		get() {
			return find {
				it.isSelected
			}
		}

	fun select(text: String, fire: Boolean) {
		val v = itemList.find { it.text == text }
		if (v != null) {
			clickTab(v, fire)
		}
	}

	private fun clickTab(v: TabBarItemView, fire: Boolean) {
		val old = itemList.find { it.isSelected }
		if (old != null) {
			if (old == v) {
				onReselect(old)
				return
			} else {
				onUnselect(old)
			}
		}
		itemList.forEach {
			it.isSelected = false
		}
		v.isSelected = true
		if (fire) {
			onSelect(v)
		}
	}

	fun tab(text: String, resId: Int): TabBarItemView {
		val v = TabBarItemView(context)
		v.autoTintDrawable = this.autoTintDrawable
		v.setText(text)
		v.setIcon(resId)
		v.tabIndex = itemList.size
		itemList += v
		return v
	}

	fun tab(text: String, drawable: Drawable): TabBarItemView {
		val v = TabBarItemView(context)
		v.autoTintDrawable = this.autoTintDrawable
		v.setText(text)
		v.setIcon(drawable)
		v.tabIndex = itemList.size
		itemList += v
		return v
	}

	companion object {
		const val HEIGHT = 50// dp
	}
}
