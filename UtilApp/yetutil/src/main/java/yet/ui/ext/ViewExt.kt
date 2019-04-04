package yet.ui.ext

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import yet.ext.ColorDrawable
import yet.theme.Colors
import yet.theme.Space
import yet.ui.res.D
import yet.ui.res.RectDraw
import yet.ui.res.Shapes
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 16/3/12.
 */

private val atomInt = AtomicInteger(1)

fun genViewId(): Int {
	while (true) {
		val n = atomInt.get()
		var newVal = n + 1
		if (newVal > 0x00FFFFFF) newVal = 1
		if (atomInt.compareAndSet(n, newVal)) {
			return n
		}
	}
}

val View.asTextView: TextView
	get() {
		return this as TextView
	}
val View.asButton: Button
	get() {
		return this as Button
	}

fun View.child(n: Int): View? {
	val g = this as? ViewGroup ?: return null
	if (g.childCount > n) {
		return g.getChildAt(n)
	}
	return null
}

@Suppress("UNCHECKED_CAST")
fun <T : View> View.child(cls: KClass<T>): T? {
	val g = this as? ViewGroup ?: return null
	for (c in g.childViews) {
		if (c::class == cls) {
			return c as T
		}
	}
	return null
}


fun <T : View> T.genId(): T {
	id = genViewId()
	return this
}

fun <T : View> T.gone(): T {
	visibility = View.GONE
	return this
}

fun <T : View> T.visiable(): T {
	visibility = View.VISIBLE
	return this
}

fun <T : View> T.invisiable(): T {
	visibility = View.INVISIBLE
	return this
}

fun <T : View> T.isGone(): Boolean {
	return visibility == View.GONE
}

fun <T : View> T.isVisiable(): Boolean {
	return visibility == View.VISIBLE
}

fun <T : View> T.isInvisiable(): Boolean {
	return visibility == View.INVISIBLE
}


fun <T : View> T.padding(left: Int, top: Int, right: Int, bottom: Int): T {
	this.setPadding(dp(left), dp(top), dp(right), dp(bottom));
	return this
}

fun <T : View> T.padding(p: Int): T {
	this.setPadding(dp(p), dp(p), dp(p), dp(p))
	return this
}

fun <T : View> T.paddingNormal(): T {
	this.padding(Space.Normal)
	return this
}

fun <T : View> T.paddingNormalSmall(): T {
	this.padding(Space.Normal, Space.Small, Space.Normal, Space.Small)
	return this
}

fun <T : View> T.paddingNormalTiny(): T {
	this.padding(Space.Normal, Space.Tiny, Space.Normal, Space.Tiny)
	return this
}

fun <T : View> T.paddingSmall(): T {
	this.padding(Space.Small)
	return this
}

fun <T : View> T.paddingSmallTiny(): T {
	this.padding(Space.Small, Space.Tiny, Space.Small, Space.Tiny)
	return this
}

fun <T : View> T.paddingTiny(): T {
	this.padding(Space.Tiny)
	return this
}

fun <T : View> T.padLeft(n: Int): T {
	this.setPadding(dp(n), this.paddingTop, this.paddingRight, this.paddingBottom)
	return this
}

fun <T : View> T.padTop(n: Int): T {
	this.setPadding(this.paddingLeft, dp(n), this.paddingRight, this.paddingBottom)
	return this
}

fun <T : View> T.padRight(n: Int): T {
	this.setPadding(this.paddingLeft, this.paddingTop, dp(n), this.paddingBottom)
	return this
}

fun <T : View> T.padBottom(n: Int): T {
	this.setPadding(this.paddingLeft, this.paddingTop, this.paddingRight, dp(n))
	return this
}

fun <T : View> T.backColor(color: Int): T {
	setBackgroundColor(color)
	return this
}

fun <T : View> T.backColor(color: Int, fadeColor: Int): T {
	//    background = colorDrawable(color, fadeColor)
	this.background = ColorDrawable(color, fadeColor)
	return this
}


fun <T : View> T.backColorWhite(): T {
	setBackgroundColor(Colors.WHITE)
	return this
}

fun <T : View> T.backColorTrans(): T {
	setBackgroundColor(Colors.TRANS)
	return this
}

fun <T : View> T.backColorTheme(): T {
	backColor(Colors.Theme)
	return this
}

fun <T : View> T.backColorThemeFade(): T {
	backColor(Colors.Theme, Colors.Fade)
	return this
}

fun <T : View> T.backColorWhiteFade(): T {
	backColor(Colors.WHITE, Colors.Fade)
	return this
}

fun <T : View> T.backColorTransFade(): T {
	backColor(Colors.TRANS, Colors.Fade)
	return this
}

fun <T : View> T.backColorPage(): T {
	setBackgroundColor(Colors.PageGray)
	return this
}

fun <T : View> T.backFillFade(fillColor: Int, corner: Int): T {
	backDrawable(RectDraw(fillColor).corner(corner).value, RectDraw(Colors.Fade).corner(corner).value)
	return this
}

fun <T : View> T.backFill(fillColor: Int, corner: Int): T {
	val d = RectDraw(fillColor).corner(corner)
	backDrawable(d.value)
	return this
}

fun <T : View> T.backStrike(fillColor: Int, corner: Int, borderWidth: Int, borderColor: Int): T {
	val d = RectDraw(fillColor).corner(corner).stroke(borderWidth, borderColor)
	backDrawable(d.value)
	return this
}

fun <T : View> T.backDrawable(@DrawableRes resId: Int): T {
	this.setBackgroundResource(resId)
	return this
}

fun <T : View> T.backDrawable(d: Drawable): T {
	this.background = d
	return this
}

fun <T : View> T.backDrawable(block: Shapes.RectOption.() -> Unit): T {
	this.background = Shapes.rect(block)
	return this
}

fun <T : View> T.backDrawable(normal: Drawable, pressed: Drawable): T {
	this.background = D.light(normal, pressed)
	return this
}

fun <T : View> T.backDrawable(@DrawableRes resId: Int, @DrawableRes pressed: Int): T {
	this.background = D.light(resId, pressed)
	return this
}

fun View.makeClickable(): View {
	this.isClickable = true
	return this
}

fun View.clickable(): View {
	this.isClickable = true
	return this
}

fun <T : View> T.setLinearParam(f: LinearLayout.LayoutParams.() -> LinearLayout.LayoutParams): T {
	val lp = linearParam(f)
	this.layoutParams = lp
	return this;
}


