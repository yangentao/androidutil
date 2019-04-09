package dev.entao.ui.list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.BaseAdapter
import android.widget.ListView
import dev.entao.appbase.ex.Colors
import dev.entao.ui.ext.backColorWhite
import dev.entao.ui.ext.genId
import dev.entao.ui.res.D

/**
 */
open class AnyListView(context: Context) : ListView(context) {
	var onItemClick: (item: Any) -> Unit = {}
	var onItemClick2: (View, Any, Int) -> Unit = { _, _, _ -> }

	init {
		genId()
		this.backColorWhite()
		cacheColorHint = 0
		selector = D.lightColor(Color.TRANSPARENT, Colors.Fade)
		this.setOnItemClickListener { _, view, position, _ ->
			onItemClick(getItem(position))
			onItemClick2(view, getItem(position), position)
		}
	}


	fun getItem(position: Int): Any {
		return adapter.getItem(position)
	}

	fun notifyDataSetChanged() {
		(adapter as BaseAdapter).notifyDataSetChanged()
	}

}
