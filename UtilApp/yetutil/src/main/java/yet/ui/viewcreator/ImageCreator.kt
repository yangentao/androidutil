package yet.ui.viewcreator

import android.app.Fragment
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import yet.ui.ext.genId
import yet.ui.ext.scaleCenterCrop

/**
 * Created by entaoyang@163.com on 2018-03-14.
 */

//Image View

fun ViewGroup.imageView(param: ViewGroup.LayoutParams, block: ImageView.() -> Unit): ImageView {
	val v = this.createImageView()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.imageView(index: Int, param: ViewGroup.LayoutParams, block: ImageView.() -> Unit): ImageView {
	val v = this.createImageView()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.imageViewBefore(ankor: View, param: ViewGroup.LayoutParams, block: ImageView.() -> Unit): ImageView {
	return this.imageView(this.indexOfChild(ankor), param, block)
}

fun View.createImageView(): ImageView {
	return this.context.createImageView()
}

fun Fragment.createImageView(): ImageView {
	return this.activity.createImageView()
}

fun Context.createImageView(): ImageView {
	val b = ImageView(this).genId()
	b.adjustViewBounds = true
	b.scaleCenterCrop()
	return b
}


//image button
fun ViewGroup.imageButton(param: ViewGroup.LayoutParams, block: ImageButton.() -> Unit): ImageButton {
	val v = this.createImageButton()
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.imageButton(index: Int, param: ViewGroup.LayoutParams, block: ImageButton.() -> Unit): ImageButton {
	val v = this.createImageButton()
	this.addView(v, index, param)
	v.block()
	return v
}

fun ViewGroup.imageButtonBefore(ankor: View, param: ViewGroup.LayoutParams, block: ImageButton.() -> Unit): ImageButton {
	return this.imageButton(this.indexOfChild(ankor), param, block)
}

fun View.createImageButton(): ImageButton {
	return this.context.createImageButton()
}

fun Fragment.createImageButton(): ImageButton {
	return this.activity.createImageButton()
}

fun Context.createImageButton(): ImageButton {
	return ImageButton(this).genId()
}
