package yet.ui.widget

import android.content.Context
import android.widget.LinearLayout
import yet.ui.ext.*
import yet.util.Task
import java.util.*

open class SwitchView(context: Context): LinearLayout(context) {
	private val items = ArrayList<String>()
	private val viewMap = HashMap<String, SwitchItemView>()

	init {
		horizontal().backColorWhite()
		setLinearParam { widthFill().heightWrap().margins(0, 1, 0, 1) }
		this.divider(Divider().pad(3))
	}



	fun getItemView(itemName: String): SwitchItemView {
		return viewMap[itemName]!!
	}

	fun clean() {
		items.clear()
		viewMap.clear()
		removeAllViews()
	}

	fun addItems(vararg items: String) {
		for (s in items) {
			addItem(s)
		}
	}

	fun setItems(items: List<String>) {
		clean()
		for (item in items) {
			addItems(item)
		}
	}

	fun addItem(itemName: String) {
		items.add(itemName)

		val itemView = SwitchItemView(context)
		itemView.text = itemName
		onConfigItem(itemView)
		addViewParam(itemView) { width(0).weight(1f).heightFill() }
		itemView.onClick { v->
			val s = v.text
			Task.fore {
				select(s, true)
			}
		}
		viewMap.put(itemName, itemView)
	}

	fun select(name: String, fire: Boolean = true) {
		for (s in items) {
			viewMap[s]?.isSelected = false
		}
		viewMap[name]?.isSelected = true
		if (fire) {
			onSelectChanged(items.indexOf(name), name)
		}
	}

	open fun onSelectChanged(index: Int, itemName: String) {

	}
	open fun onConfigItem(itemView: SwitchItemView) {

	}

}
