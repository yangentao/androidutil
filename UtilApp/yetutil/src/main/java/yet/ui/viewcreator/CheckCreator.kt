package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import yet.ui.ext.genId
import yet.ui.ext.textColorMajor
import yet.ui.res.D

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */


//check box
fun ViewGroup.checkBox(param: ViewGroup.LayoutParams, block: CheckBox.() -> Unit): CheckBox {
	val v = this.createCheckBox()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.checkBox(index: Int, param: ViewGroup.LayoutParams, block: CheckBox.() -> Unit): CheckBox {
	val v = this.createCheckBox()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.checkBoxBefore(ankor: View, param: ViewGroup.LayoutParams, block: CheckBox.() -> Unit): CheckBox {
	return this.checkBox(this.indexOfChild(ankor), param, block)
}

fun View.createCheckBox(): CheckBox {
	return this.context.createCheckBox()
}

fun Fragment.createCheckBox(): CheckBox {
	return this.activity.createCheckBox()
}

fun Context.createCheckBox(): CheckBox {
	val cb =  CheckBox(this).genId()
	cb.textColorMajor()
	cb.buttonDrawable = D.CheckBox
	return cb
}





