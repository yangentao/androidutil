package dev.entao.ui.list

import android.content.Context
import android.view.View
import dev.entao.ui.ext.textS
import dev.entao.ui.list.check.CheckView
import dev.entao.ui.list.views.TextItemView

/**
 */
class CheckListView(context: Context) : AnyListView(context) {
	val anyAdapter = AnyAdapter()
	var onNewView: (Context, Int) -> View = { c, _ ->
		TextItemView(c)
	}
	var onBindView: (View, Int) -> Unit = { v, p ->
		v as TextItemView
		v.textS = getItem(p).toString()
	}

	init {
		anyAdapter.onNewView = { c, p ->
			val v = this@CheckListView.onNewView(c, p)
			CheckView(c).bind(v)
		}
		anyAdapter.onBindView = { v, p ->
			v as CheckView
			v.isChecked = anyAdapter.isChecked(p)
			this@CheckListView.onBindView(v.view, p)
		}

		onItemClick2 = { v, _, p ->
			v as CheckView
			v.toggle()
			val c = v.isChecked
			anyAdapter.checkPosition(p, c)
		}
		adapter = anyAdapter
	}

	fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}
}
