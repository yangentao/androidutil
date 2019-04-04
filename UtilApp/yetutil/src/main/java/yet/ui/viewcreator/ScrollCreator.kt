package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import yet.ui.ext.genId

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


//ScrollView
fun ViewGroup.scroll(param: ViewGroup.LayoutParams, block: ScrollView.() -> Unit): ScrollView {
	val v = this.createScroll()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.scroll(index: Int, param: ViewGroup.LayoutParams, block: ScrollView.() -> Unit): ScrollView {
	val v = this.createScroll()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.scrollBefore(ankor: View, param: ViewGroup.LayoutParams, block: ScrollView.() -> Unit): ScrollView {
	return this.scroll(this.indexOfChild(ankor), param, block)
}

fun View.createScroll(): ScrollView {
	return this.context.createScroll()
}

fun Fragment.createScroll(): ScrollView {
	return this.activity.createScroll()
}

fun Context.createScroll(): ScrollView {
	return ScrollView(this).genId()
}


