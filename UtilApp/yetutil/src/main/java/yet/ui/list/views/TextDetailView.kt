package yet.ui.list.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import dev.entao.appbase.ex.dp
import dev.entao.appbase.ex.sized
import yet.ui.ext.*
import yet.ui.res.D
import yet.ui.res.Res
import yet.ui.viewcreator.textView

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


open class TextDetailView(context: Context) : HorItemView(context) {
	var textView: TextView
	var detailView: TextView

	var argS: String = ""
	var argN: Int = 0

	init {
		padding(20, 10, 20, 10).gravityCenterVertical()

		textView = textView(lParam().widthWrap().heightWrap().gravityLeftCenter()) {
			textSizeB().textColorMajor().singleLine()
		}
		addFlex()
		detailView = textView(lParam().wrap().gravityRightCenter()) {
			textSizeC().textColorMid().gravityRightCenter().multiLine()
			maxLines(2)
		}
	}

	fun setText(text: String?) {
		textView.text = text
	}

	fun setDetail(detail: String?) {
		detailView.text = detail
	}

	fun setValues(text: String?, detail: String?): TextDetailView {
		textView.text = text
		detailView.text = detail
		return this
	}

	fun setTextSize(sp1: Int, sp2: Int): TextDetailView {
		textView.textSizeSp(sp1)
		detailView.textSizeSp(sp2)
		return this
	}

	fun setLeftImage(d: Drawable) {
		textView.setCompoundDrawables(d, null, null, null)
		textView.compoundDrawablePadding = dp(10)
	}


	fun setRightImage(d: Drawable) {
		detailView.setCompoundDrawables(null, null, d, null)
		detailView.compoundDrawablePadding = dp(10)
	}

	fun rightImageMore() {
		val d = D.res(Res.more).sized(12)
		setRightImage(d)
	}
}

fun ViewGroup.textDetail(param: ViewGroup.LayoutParams, block: TextDetailView.() -> Unit): TextDetailView {
	val v = TextDetailView(this.context)
	this.addView(v, param)
	v.block()
	return v
}

fun ViewGroup.textDetailViewTrans(param: ViewGroup.LayoutParams, block: TextDetailView.() -> Unit): TextDetailView {
	val v = TextDetailView(this.context)
	this.addView(v, param)
	v.backColorTrans()
	v.textView.textColorWhite()
	v.detailView.textColorWhite()
	v.block()
	return v
}