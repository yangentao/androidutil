package dev.entao.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dev.entao.ui.ext.*
/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun ViewGroup.textView(block: TextView.() -> Unit): TextView {
	val v = this.createTextView()
	this.addView(v)
	v.block()
	return v
}

//TextView
fun ViewGroup.textView(param: ViewGroup.LayoutParams, block: TextView.() -> Unit): TextView {
	val v = this.createTextView()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.textView(index: Int, param: ViewGroup.LayoutParams, block: TextView.() -> Unit): TextView {
	val v = this.createTextView()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.textViewBefore(ankor: View, param: ViewGroup.LayoutParams, block: TextView.() -> Unit): TextView {
	return this.textView(this.indexOfChild(ankor), param, block)
}

fun View.createTextView(): TextView {
	return this.context.createTextView()
}

fun Context.createTextView(): TextView {
	return TextView(this).genId().gravityLeftCenter().textSizeB().textColorMajor()
}

fun Fragment.createTextView(): TextView {
	return this.activity.createTextView()
}

fun View.createTextViewA(): TextView {
	return this.createTextView().textSizeA()
}

fun Context.createTextViewA(): TextView {
	return this.createTextView().textSizeA()
}

fun Fragment.createTextViewA(): TextView {
	return this.createTextView().textSizeA()
}

fun View.createTextViewB(): TextView {
	return this.createTextView().textSizeB()
}

fun Fragment.createTextViewB(): TextView {
	return this.createTextView().textSizeB()
}

fun Context.createTextViewB(): TextView {
	return this.createTextView().textSizeB()
}

fun View.createTextViewC(): TextView {
	return this.createTextView().textSizeC().textColorMinor()
}

fun Fragment.createTextViewC(): TextView {
	return this.createTextView().textSizeC().textColorMinor()
}

fun Context.createTextViewC(): TextView {
	return this.createTextView().textSizeC().textColorMinor()
}

fun View.createTextViewD(): TextView {
	return this.createTextView().textSizeD().textColorMinor()
}

fun Fragment.createTextViewD(): TextView {
	return this.createTextView().textSizeD().textColorMinor()
}

fun Context.createTextViewD(): TextView {
	return this.createTextView().textSizeD().textColorMinor()
}
