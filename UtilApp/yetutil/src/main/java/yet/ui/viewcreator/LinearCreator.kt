package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import yet.ui.ext.*

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


//LinearLayout
fun ViewGroup.linear(param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.createLinear()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.linear(index: Int, param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.createLinear()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.linearBefore(ankor: View, param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	return this.linear(this.indexOfChild(ankor), param, block)
}

fun ViewGroup.linearHor(param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearHorizontal()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.linearHor(index: Int, param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearHorizontal()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.linearHorBefore(ankor: View, param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	return this.linearHor(this.indexOfChild(ankor), param, block)
}

fun ViewGroup.linearVer(param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearVertical()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.linearVer(index: Int, param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	val v = this.context.createLinearVertical()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.linearVerBefore(ankor: View, param: ViewGroup.LayoutParams, block: LinearLayout.() -> Unit): LinearLayout {
	return this.linearVer(this.indexOfChild(ankor), param, block)
}

fun View.createLinear(): LinearLayout {
	return this.context.createLinear()
}

fun Fragment.createLinear(): LinearLayout {
	return this.activity.createLinear()
}

fun Context.createLinear(): LinearLayout {
	return LinearLayout(this).genId()
}


fun View.createLinearVertical(): LinearLayout {
	return this.context.createLinearVertical()
}

fun Fragment.createLinearVertical(): LinearLayout {
	return this.activity.createLinearVertical()
}

fun Context.createLinearVertical(): LinearLayout {
	return LinearLayout(this).genId().vertical()
}

fun View.createLinearHorizontal(): LinearLayout {
	return this.context.createLinearHorizontal()
}

fun Fragment.createLinearHorizontal(): LinearLayout {
	return this.activity.createLinearHorizontal()
}

fun Context.createLinearHorizontal(): LinearLayout {
	return LinearLayout(this).genId().horizontal()
}

