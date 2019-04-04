package yet.ui.list

import android.content.Context

/**
 */
class SimpleListView(context: Context) : AnyListView(context) {
	val anyAdapter = AnyAdapter()

	init {
		adapter = anyAdapter
	}

	fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}
}
