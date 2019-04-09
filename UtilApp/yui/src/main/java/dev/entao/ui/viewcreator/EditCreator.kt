package dev.entao.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import dev.entao.appbase.ex.color
import dev.entao.ui.ext.*
import dev.entao.ui.res.D
import dev.entao.ui.widget.EditTextX

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

fun ViewGroup.edit(block: EditText.() -> Unit): EditText {
	val v = this.createEdit()
	this.addView(v)
	v.block()
	return v
}

//EditText
fun ViewGroup.edit(param: ViewGroup.LayoutParams, block: EditText.() -> Unit): EditText {
	val v = this.createEdit()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.edit(index: Int, param: ViewGroup.LayoutParams, block: EditText.() -> Unit): EditText {
	val v = this.createEdit()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.editBefore(ankor: View, param: ViewGroup.LayoutParams, block: EditText.() -> Unit): EditText {
	return this.edit(this.indexOfChild(ankor), param, block)
}

fun View.createEdit(): EditText {
	return this.context.createEdit()
}

fun Fragment.createEdit(): EditText {
	return this.activity.createEdit()
}

fun Context.createEdit(): EditText {
	val ed = EditText(this).genId().singleLine()
	ed.setHintTextColor(0x808080.color)
	ed.textColorMajor()
	ed.textSizeB().gravityLeftCenter().backDrawable(D.Input).padding(8, 2, 8, 2)
	return ed
}


//EditTextX
fun ViewGroup.editX(param: ViewGroup.LayoutParams, block: EditTextX.() -> Unit): EditTextX {
	val v = this.createEditX()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.editX(index: Int, param: ViewGroup.LayoutParams, block: EditTextX.() -> Unit): EditTextX {
	val v = this.createEditX()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.editXBefore(ankor: View, param: ViewGroup.LayoutParams, block: EditTextX.() -> Unit): EditTextX {
	return this.editX(this.indexOfChild(ankor), param, block)
}

fun View.createEditX(): EditTextX {
	return this.context.createEditX()
}

fun Fragment.createEditX(): EditTextX {
	return this.activity.createEditX()
}

fun Context.createEditX(): EditTextX {
	val ed = EditTextX(this).genId().singleLine()
	ed.setHintTextColor(0x808080.color)
	ed.textSizeB().gravityLeftCenter().backDrawable(D.Input).padding(8, 2, 8, 2)
	return ed
}


//EditArea
fun ViewGroup.editArea(param: ViewGroup.LayoutParams, block: EditText.() -> Unit): EditText {
	val v = this.createEditArea()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.editArea(index: Int, param: ViewGroup.LayoutParams, block: EditText.() -> Unit): EditText {
	val v = this.createEditArea()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.editAreaBefore(ankor: View, param: ViewGroup.LayoutParams, block: EditText.() -> Unit): EditText {
	return this.editArea(this.indexOfChild(ankor), param, block)
}

fun View.createEditArea(): EditText {
	return this.context.createEditArea()
}

fun Fragment.createEditArea(): EditText {
	return this.activity.createEditArea()
}

fun Context.createEditArea(): EditText {
	val ed = EditText(this).genId()
	ed.setHintTextColor(0x808080.color)
	ed.textSizeB().gravityTopLeft().backDrawable(D.Input).padding(10, 5, 10, 5).multiLine()
	return ed
}
