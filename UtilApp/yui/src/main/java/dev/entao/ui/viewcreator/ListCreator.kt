package dev.entao.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import dev.entao.appbase.ex.Colors
import dev.entao.ui.ext.genId
import dev.entao.ui.list.SimpleListView
import dev.entao.ui.res.D

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun  ViewGroup.simpleListView(param: ViewGroup.LayoutParams, block: SimpleListView.() -> Unit): SimpleListView {
	val lv = SimpleListView(context)
	this.addView(lv, param)
	lv.block()
	return lv
}

//List View
fun ViewGroup.listView(param: ViewGroup.LayoutParams, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.listView(index: Int, param: ViewGroup.LayoutParams, block: ListView.() -> Unit): ListView {
	val v = this.createListView()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.listViewBefore(ankor: View, param: ViewGroup.LayoutParams, block: ListView.() -> Unit): ListView {
	return this.listView(this.indexOfChild(ankor), param, block)
}


fun View.createListView(): ListView {
	return this.context.createListView()
}

fun Fragment.createListView(): ListView {
	return this.activity.createListView()
}

fun Context.createListView(): ListView {
	val lv = ListView(this).genId()
	lv.cacheColorHint = 0
	lv.selector = D.lightColor(Color.TRANSPARENT, Colors.Fade)
	return lv
}
