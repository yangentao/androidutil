package dev.entao.ui.grid

import android.content.Context
import android.widget.AdapterView
import android.widget.LinearLayout
import dev.entao.ui.ext.*
import dev.entao.ui.list.AnyAdapter
import dev.entao.ui.page.TitlePage

/**
 * Created by entaoyang@163.com on 2016-08-24.
 */

abstract class GridPage : TitlePage() {
	lateinit var gridView: LineGridView
	lateinit var anyAdapter: AnyAdapter

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		gridView = LineGridView(context)
		gridView.numColumns = 2
		gridView.horizontalSpacing = 0
		gridView.verticalSpacing = 0
		contentView.addViewParam(gridView) {
			widthFill().height(0).weight(1)
		}

		anyAdapter.onBindView = { v, p ->
			v as GridItemView
			onBindItemView(v, anyAdapter.getItem(p))
		}
		anyAdapter.onNewView = { c, _ ->
			val v = GridItemView(c)
			v.padding(10)
			v
		}
		anyAdapter.onRequestItems = this::onRequestItems

		gridView.adapter = anyAdapter
		gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
			onItemClick(anyAdapter.getItem(pos))
		}

	}

	open fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}


	open fun onItemClick(item: Any) {

	}


	abstract fun onBindItemView(view: GridItemView, item: Any)

	fun requestItems() {
		anyAdapter.requestItems()
	}

	open fun onRequestItems(): List<Any> {
		return emptyList()
	}
}