package dev.entao.ui.ext

import android.view.View
import android.view.ViewGroup
import dev.entao.appbase.ex.dp
import dev.entao.theme.Space
import dev.entao.theme.ViewSize

/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */


val Param: ViewGroup.LayoutParams
	get() {
		return ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	}
val MParam: ViewGroup.MarginLayoutParams
	get() {
		return ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT)
	}


fun <T : ViewGroup.LayoutParams> T.set(view: View) {
	view.layoutParams = this
}


val VGParam: ViewGroup.LayoutParams
	get() {
		return ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
	}

fun layoutParam(): ViewGroup.LayoutParams {
	return ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
}


fun <T : ViewGroup.LayoutParams> T.size(w: Int, h: Int = w): T {
	return widthDp(w).heightDp(h)
}

fun <T : ViewGroup.LayoutParams> T.width_(w: Int): T {
	if (w > 0) {
		this.width = dp(w)
	} else {
		this.width = w
	}
	return this
}

fun <T : ViewGroup.LayoutParams> T.width(w: Int): T {
	if (w > 0) {
		this.width = dp(w)
	} else {
		this.width = w
	}
	return this
}

fun <T : ViewGroup.LayoutParams> T.widthDp(w: Int): T {
	if (w > 0) {
		this.width = dp(w)
	} else {
		this.width = w
	}
	return this
}

fun <T : ViewGroup.LayoutParams> T.widthPx(w: Int): T {
	this.width = w
	return this
}


fun <T : ViewGroup.LayoutParams> T.widthWrap(): T {
	this.width = ViewGroup.LayoutParams.WRAP_CONTENT
	return this
}

val <T : ViewGroup.LayoutParams> T.WidthWrap: T
	get() {
		this.width = ViewGroup.LayoutParams.WRAP_CONTENT
		return this
	}

fun <T : ViewGroup.LayoutParams> T.widthFill(): T {
	this.width = ViewGroup.LayoutParams.MATCH_PARENT
	return this
}

val <T : ViewGroup.LayoutParams> T.WidthFill: T
	get() {
		this.width = ViewGroup.LayoutParams.MATCH_PARENT
		return this
	}

fun <T : ViewGroup.LayoutParams> T.heightPx(h: Int): T {
	this.height = h
	return this
}

fun <T : ViewGroup.LayoutParams> T.height(h: Int): T {
	if (h > 0) {
		this.height = dp(h)
	} else {
		this.height = h
	}
	return this
}

fun <T : ViewGroup.LayoutParams> T.height_(h: Int): T {
	if (h > 0) {
		this.height = dp(h)
	} else {
		this.height = h
	}
	return this
}

fun <T : ViewGroup.LayoutParams> T.heightDp(h: Int): T {
	if (h > 0) {
		this.height = dp(h)
	} else {
		this.height = h
	}
	return this
}

fun <T : ViewGroup.LayoutParams> T.heightWrap(): T {
	this.height = ViewGroup.LayoutParams.WRAP_CONTENT
	return this
}

val <T : ViewGroup.LayoutParams> T.HeightWrap: T
	get() {
		this.height = ViewGroup.LayoutParams.WRAP_CONTENT
		return this
	}

fun <T : ViewGroup.LayoutParams> T.heightFill(): T {
	this.height = ViewGroup.LayoutParams.MATCH_PARENT
	return this
}

val <T : ViewGroup.LayoutParams> T.HeightFill: T
	get() {
		this.height = ViewGroup.LayoutParams.MATCH_PARENT
		return this
	}

fun <T : ViewGroup.LayoutParams> T.wrap(): T {
	this.height = ViewGroup.LayoutParams.WRAP_CONTENT
	this.width = ViewGroup.LayoutParams.WRAP_CONTENT
	return this
}

val <T : ViewGroup.LayoutParams> T.Wrap: T
	get() {
		this.height = ViewGroup.LayoutParams.WRAP_CONTENT
		this.width = ViewGroup.LayoutParams.WRAP_CONTENT
		return this
	}

fun <T : ViewGroup.LayoutParams> T.fill(): T {
	this.width = ViewGroup.LayoutParams.MATCH_PARENT
	this.height = ViewGroup.LayoutParams.MATCH_PARENT
	return this
}

val <T : ViewGroup.LayoutParams> T.Fill: T
	get() {
		this.width = ViewGroup.LayoutParams.MATCH_PARENT
		this.height = ViewGroup.LayoutParams.MATCH_PARENT
		return this
	}


val <T : ViewGroup.LayoutParams> T.HeightButton: T
	get() {
		return heightDp(ViewSize.ButtonHeight)
	}

val <T : ViewGroup.LayoutParams> T.HeightBar: T
	get() {
		return heightDp(ViewSize.BarHeight)
	}

fun <T : ViewGroup.LayoutParams> T.heightButtonSmall(): T {
	return heightDp(ViewSize.ButtonHeightSmall)
}

val <T : ViewGroup.LayoutParams> T.HeightButtonSmall: T
	get() {
		return heightDp(ViewSize.ButtonHeightSmall)
	}

fun <T : ViewGroup.LayoutParams> T.heightEdit(): T {
	return heightDp(ViewSize.EditHeight)
}

val <T : ViewGroup.LayoutParams> T.HeightEdit: T
	get() {
		return heightDp(ViewSize.EditHeight)
	}

fun <T : ViewGroup.LayoutParams> T.heightEditSmall(): T {
	return heightDp(ViewSize.EditHeightSmall)
}

val <T : ViewGroup.LayoutParams> T.HeightEditSmall: T
	get() {
		return heightDp(ViewSize.EditHeightSmall)
	}

fun <T : ViewGroup.LayoutParams> T.heightEditSearch(): T {
	return heightDp(ViewSize.EditHeightSearch)
}

val <T : ViewGroup.LayoutParams> T.HeightEditSearch: T
	get() {
		return heightDp(ViewSize.EditHeightSearch)
	}


fun <T : ViewGroup.MarginLayoutParams> T.marginLeft(v: Int): T {
	this.leftMargin = dp(v)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginRight(v: Int): T {
	this.rightMargin = dp(v)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginTop(v: Int): T {
	this.topMargin = dp(v)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginBottom(v: Int): T {
	this.bottomMargin = dp(v)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.margins(left: Int, top: Int, right: Int, bottom: Int): T {
	this.setMargins(dp(left), dp(top), dp(right), dp(bottom))
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginsPx(left: Int, top: Int, right: Int, bottom: Int): T {
	this.setMargins(left, top, right, bottom)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.margins(hor: Int, ver: Int): T {
	return margins(hor, ver, hor, ver)
}

fun <T : ViewGroup.MarginLayoutParams> T.marginX(left: Int, right: Int): T {
	this.setMargins(dp(left), this.topMargin, dp(right), this.bottomMargin)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginX(x: Int): T {
	this.setMargins(dp(x), this.topMargin, dp(x), this.bottomMargin)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginY(top: Int, bottom: Int): T {
	this.setMargins(this.leftMargin, dp(top), this.rightMargin, dp(bottom))
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginY(y: Int): T {
	this.setMargins(this.leftMargin, dp(y), this.rightMargin, dp(y))
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.margins(m: Int): T {
	val v = dp(m)
	this.setMargins(v, v, v, v)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginNormal(): T {
	this.margins(Space.Normal)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginNormalTiny(): T {
	this.margins(Space.Normal, Space.Tiny, Space.Normal, Space.Tiny)
	return this
}

fun <T : ViewGroup.MarginLayoutParams> T.marginNormalSmall(): T {
	this.margins(Space.Normal, Space.Small, Space.Normal, Space.Small)
	return this
}

