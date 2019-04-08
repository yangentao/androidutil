package yet.ui.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.res.ColorList
import yet.ui.res.ImageStated
import yet.ui.res.RectDraw
import yet.ui.res.Shapes
import yet.util.Task
import java.util.*

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

class SwitchPanel(context: Context) : LinearLayout(context) {

	private val items = ArrayList<String>()
	var selectIndex = 0
		private set

	private var callback: SwitchSelectChangeListener? = null

	init {
		genId()
		horizontal()
		backDrawable(Shapes.rect {
			cornerPx = CORNER
			fillColor = Colors.WHITE
		})

	}

	val selectText: String?
		get() {
			if (selectIndex >= 0 && selectIndex < items.size) {
				return items[selectIndex]
			}
			return null
		}

	fun select(index: Int) {
		if (index >= 0 && index < items.size) {
			for (i in 0..this.childCount - 1) {
				val view = this.getChildAt(i)
				if (i == index) {
					if (!view.isSelected) {
						selectIndex = i
						view.isSelected = true
					}
				} else {
					if (view.isSelected) {
						view.isSelected = false
					}
				}
			}
		}
	}

	fun setSelectCallback(callback: SwitchSelectChangeListener) {
		this.callback = callback
	}

	fun selectItem(item: String) {
		val index = items.indexOf(item)
		select(index)
	}

	fun setItems(items: List<String>?) {
		this.items.clear()
		if (items != null) {
			this.items.addAll(items)
		}
		rebuild()
	}

	private val clickListener = View.OnClickListener { v ->
		val textView = v as TextView
		val text = textView.text.toString()
		Task.fore{
			selectItem(text)
			if (callback != null) {
				callback!!.onSwitchItemSelectChanged(text)
			}
		}
	}

	fun rebuild() {
		this.removeAllViews()
		if (items.isEmpty()) {
			return
		}
		if (items.size == 1) {
			val text = items[0]
			addItemOne(text).setOnClickListener(clickListener)
			return
		}
		if (items.size == 2) {
			val text1 = items[0]
			val text2 = items[1]
			addItemLeft(text1).setOnClickListener(clickListener)
			addItemRight(text2).setOnClickListener(clickListener)
			return
		}
		addItemLeft(items[0]).setOnClickListener(clickListener)
		for (i in 1..items.size - 1 - 1) {
			addItemMid(i).setOnClickListener(clickListener)
		}
		addItemRight(items[items.size - 1]).setOnClickListener(clickListener)
	}

	private fun addItemMid(index: Int): SwitchPanelItem {
		val v = SwitchPanelItem(context).text(items[index]).styleMid()
		v.isSelected = selectIndex == index
		addView(v, linearParam().width(0).weight(1f).heightFill().gravityCenter().margins(1))
		return v
	}

	private fun addItemRight(text: String): SwitchPanelItem {
		val v = SwitchPanelItem(context).text(text).styleRight()
		v.isSelected = selectIndex == items.size - 1
		addView(v, linearParam().width_(0).weight(1f).heightFill().gravityCenter().margins(1))
		return v
	}

	private fun addItemLeft(text: String): SwitchPanelItem {
		val v = SwitchPanelItem(context).text(text).styleLeft()
		v.isSelected = selectIndex == 0
		addView(v, linearParam().width(0).weight(1f).heightFill().gravityCenter().margins(1))
		return v
	}

	private fun addItemOne(text: String): SwitchPanelItem {
		val v = SwitchPanelItem(context).text(text).styleOne()
		addView(v, linearParam().widthFill().heightFill().gravityCenter().margins(1))
		v.isSelected = true
		return v
	}

	class SwitchPanelItem(context: Context) : TextView(context) {
		private val corner = CORNER - 1

		init {
			textSizeC().gravityCenter()
		}

		fun text(text: String): SwitchPanelItem {
			this.text = text
			return this
		}

		fun styleOne(): SwitchPanelItem {
			backDrawable(RectDraw(Colors.WHITE).corner(corner).value).textColor(Colors.Theme)
			return this
		}

		fun styleLeft(): SwitchPanelItem {
			val d = ImageStated(RectDraw(Colors.Theme).corners(corner, 0, 0, corner).value).selected(
					RectDraw(Colors.WHITE).corners(corner, 0, 0, corner).value).value
			backDrawable(d).textColor(ColorList(Colors.WHITE).selected(Colors.Theme).value)
			return this
		}

		fun styleRight(): SwitchPanelItem {
			val d = ImageStated(RectDraw(Colors.Theme).corners(0, corner, corner, 0).value)
					.selected(RectDraw(Colors.WHITE).corners(0, corner, corner, 0).value).value
			backDrawable(d).textColor(ColorList(Colors.WHITE).selected(Colors.Theme).get())
			return this
		}

		fun styleMid(): SwitchPanelItem {
			val d = ImageStated(RectDraw(Colors.Theme).value).selected(
					RectDraw(Colors.WHITE).value).get()
			backDrawable(d).textColor(ColorList(Colors.WHITE).selected(Colors.Theme).get())
			return this
		}
	}


	interface SwitchSelectChangeListener {
		fun onSwitchItemSelectChanged(text: String)
	}

	companion object {
		private val CORNER = 6
	}

}
