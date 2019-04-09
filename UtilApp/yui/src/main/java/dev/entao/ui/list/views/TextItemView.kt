package dev.entao.ui.list.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.TextView
import dev.entao.appbase.ex.sized
import dev.entao.theme.IconSize
import dev.entao.theme.Space
import dev.entao.ui.ext.gravityLeftCenter
import dev.entao.ui.ext.padding
import dev.entao.ui.ext.textColorMajor
import dev.entao.ui.ext.textSizeA
import dev.entao.ui.res.D

/**
 * Created by entaoyang@163.com on 16/3/13.
 */
class TextItemView(context: Context) : TextView(context) {
	init {
		padding(Space.Normal, Space.Small, Space.Normal, Space.Small).gravityLeftCenter().textSizeA().textColorMajor()
	}

	fun icon(d: Drawable?) {
		d?.sized(IconSize.Normal)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(d: Drawable?, size: Int) {
		d?.sized(size)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(@DrawableRes resId: Int, size: Int) {
		val d = D.sized(resId, size)
		setCompoundDrawables(d, null, null, null)
	}

	fun icon(@DrawableRes resId: Int) {
		val d = D.res(resId)
		setCompoundDrawables(d, null, null, null)
	}
}
