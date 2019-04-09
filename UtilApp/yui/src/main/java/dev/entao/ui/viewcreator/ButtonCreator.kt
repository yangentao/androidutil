package dev.entao.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import dev.entao.ui.ext.*
/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun LinearLayout.buttonGreenRound(block: Button.() -> Unit): Button {
	return this.button(LParam.WidthFill.HeightButton.margins(15)) {
		styleGreenRound()
		this.block()
	}
}

fun LinearLayout.buttonRedRound(block: Button.() -> Unit): Button {
	return this.button(LParam.WidthFill.HeightButton.margins(15)) {
		styleRedRound()
		this.block()
	}
}

//Button
fun ViewGroup.button(param: ViewGroup.LayoutParams, block: Button.() -> Unit): Button {
	val v = this.createButton()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.button(index: Int, param: ViewGroup.LayoutParams, block: Button.() -> Unit): Button {
	val v = this.createButton()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.buttonBefore(ankor: View, param: ViewGroup.LayoutParams, block: Button.() -> Unit): Button {
	return this.button(this.indexOfChild(ankor), param, block)
}

fun View.createButton(text: String = ""): Button {
	return this.context.createButton(text)
}

fun Fragment.createButton(text: String = ""): Button {
	return this.activity.createButton(text)
}

fun Context.createButton(text: String = ""): Button {
	return Button(this).genId().text(text).textSizeB().padding(3)
}
