package yet.ui.list

import android.view.View
import android.widget.AdapterView
import android.widget.ListView

/**
 * Created by yet on 2015/10/11.
 */
object ListViewUtil {
	fun addClick(listView: ListView, listener: ListViewClickListener) {
		listView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
			click(listView, view, position, listener)
		}
	}

	fun addLongClick(listView: ListView, listener: ListViewLongClickListener) {
		listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, view, position, _ ->
			longClick(listView, view, position, listener)
		}
	}

	fun click(listView: ListView, view: View, position: Int, event: ListViewClickListener) {
		val adp = listView.adapter
		val countAdapter = adp.count
		var pos = position
		if (pos >= 0 && pos < listView.headerViewsCount) {
			event.onItemClickHeader(listView, view, pos)
		}
		pos -= listView.headerViewsCount
		if (pos >= 0 && pos < countAdapter) {
			event.onItemClickAdapter(listView, view, pos)

		}
		pos -= countAdapter
		if (pos >= 0 && pos < listView.footerViewsCount) {
			event.onItemClickFooter(listView, view, pos)
		}
		event.onItemClick(listView, view, position)
	}

	fun longClick(listView: ListView, view: View, position: Int, event: ListViewLongClickListener): Boolean {
		var ret = false
		val adp = listView.adapter
		val countAdapter = adp.count

		var pos = position
		if (pos >= 0 && pos < listView.headerViewsCount) {
			ret = ret || event.onItemLongClickHeader(listView, view, pos)
		}
		pos -= listView.headerViewsCount
		if (pos >= 0 && pos < countAdapter) {
			ret = ret || event.onItemLongClickAdapter(listView, view, pos)
		}
		pos -= countAdapter
		if (pos >= 0 && pos < listView.footerViewsCount) {
			ret = ret || event.onItemLongClickFooter(listView, view, pos)
		}
		ret = ret || event.onItemLongClick(listView, view, position)
		return ret
	}


}
