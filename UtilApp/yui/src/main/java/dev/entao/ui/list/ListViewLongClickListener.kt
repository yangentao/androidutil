package dev.entao.ui.list

import android.view.View
import android.widget.ListView

/**
 * Created by yet on 2015/10/11.
 */
interface ListViewLongClickListener {

	fun onItemLongClickAdapter(listView: ListView, view: View, position: Int): Boolean

	fun onItemLongClickHeader(listView: ListView, view: View, position: Int): Boolean

	fun onItemLongClickFooter(listView: ListView, view: View, position: Int): Boolean

	fun onItemLongClick(listView: ListView, view: View, position: Int): Boolean

}
