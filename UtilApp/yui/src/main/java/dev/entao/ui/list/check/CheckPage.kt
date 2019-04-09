package dev.entao.ui.list.check

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import dev.entao.ui.ext.gone
import dev.entao.ui.ext.visiable
import dev.entao.ui.list.ListPage

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class CheckPage : ListPage() {

	var enableCheck = true
		set(value) {
			field = value
			if (contentCreated) {
				listView.invalidateViews()
				onEnableCheckChanged(value)
			}
		}


	init {
		withSearchEdit = true
	}

	open fun onEnableCheckChanged(enableCheck: Boolean) {

	}

	override fun packNewView(context: Context, view: View, position: Int): View {
		return if (enableCheck && isCheckable(position)) {
			CheckView(context).bind(view)
		} else {
			view
		}
	}

	override fun unpackBindView(itemView: View, position: Int): View {
		return if (itemView is CheckView) {
			itemView.isChecked = anyAdapter.isChecked(position)
			if(enableCheck) {
				itemView.checkView.visiable()
			}else {
				itemView.checkView.gone()
			}
			itemView.view
		} else {
			itemView
		}

	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		enablePullRefresh(false)

	}

	override fun onSearchTextChanged(s: String) {
		anyAdapter.filter {
			s in it.toString()
		}
	}

	open fun isCheckable(position: Int): Boolean {
		return true
	}

	override fun onItemClickAdapter(view: View, item: Any, position: Int) {

		if (enableCheck && view is CheckView) {
			view.toggle()
			val c = view.isChecked
			anyAdapter.checkPosition(position, c)
		}
	}

	fun checkPosition(vararg posArr: Int) {
		for (index in posArr) {
			if (isCheckable(index)) {
				anyAdapter.checkPosition(index, true)
			}
		}
	}

	fun checkItem(vararg itemArr: Any) {
		for (item in itemArr) {
			anyAdapter.checkItem(item)

		}
	}

	fun checkItem(ls: Collection<Any>) {
		for (item in ls) {
			anyAdapter.checkItem(item)
		}
	}

	fun uncheckAll() {
		anyAdapter.clearChecked()
	}

	fun checkAll() {
		anyAdapter.checkAll()
	}

	val checkedItems: List<Any>
		get() {
			return anyAdapter.checkedItems
		}


}
