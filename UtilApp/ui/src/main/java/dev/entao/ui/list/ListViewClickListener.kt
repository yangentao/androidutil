package dev.entao.ui.list

import android.view.View
import android.widget.ListView

/**
 * Created by yet on 2015/10/11.
 */
interface ListViewClickListener {
	fun onItemClickAdapter(listView: ListView, view: View, position: Int)

	fun onItemClickHeader(listView: ListView, view: View, position: Int)

	fun onItemClickFooter(listView: ListView, view: View, position: Int)

	fun onItemClick(listView: ListView, view: View, position: Int)

}
