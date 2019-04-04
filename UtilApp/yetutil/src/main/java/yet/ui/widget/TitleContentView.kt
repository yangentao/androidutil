package yet.ui.widget

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import yet.ext.color
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.viewcreator.createTextView
import yet.ui.viewcreator.textView
import yet.ui.list.views.TextDetailView

/**
 * Created by entaoyang@163.com on 2017-05-20.
 */


class TitleContentView(context: Context) : LinearLayout(context) {
	val titleView: TextView

	init {
		vertical()
		padding(10)
		backFill(defaultBackColor, defaultBackCorner)


		titleView = textView(lParam().widthFill().heightWrap().margins(0, 0, 0, 10)) {
			textColor(defaultTitleTextColor)
			textSizeA()
			gravityCenter()
		}
		if (defaultHasLine) {
			addGrayLine {
				color(defaultLineColor)
				bottom(10)
			}
		}
	}

	fun setTitle(title: String) {
		titleView.text = title
	}

	fun styleColored(color: Int) {
		titleView.textColorWhite()
		addGrayLine {
			color(Colors.WHITE)
			bottom(10)
		}
		backFill(color, 4)
	}

	companion object {
		var defaultTitleTextColor: Int = Colors.TextColorMajor
		var defaultHasLine = true
		var defaultLineColor = Colors.WHITE
		var defaultBackColor = 0xeeeeee.color
		var defaultBackCorner = 4
		var defaultTextColor = Colors.TextColorMajor
	}
}
inline fun <P : ViewGroup.LayoutParams> ViewGroup.addTitleContentView(param: P, block: TitleContentView.() -> Unit): TitleContentView {
	val v = TitleContentView(this.context)
	this.addView(v, param)
	v.block()
	return v
}

inline fun TitleContentView.addTextViewStyled(param: LinearLayout.LayoutParams, block: TextView.() -> Unit): TextView {
	val v = this.createTextView()
	this.addView(v, param)
	v.textColor(TitleContentView.defaultTextColor)
	v.block()
	return v
}

inline fun  TitleContentView.addTextDetailViewStyled(param: LinearLayout.LayoutParams, block: TextDetailView.() -> Unit): TextDetailView {
	val v = TextDetailView(this.context)
	this.addView(v, param)
	v.backColorTrans()
	v.textView.textColor(TitleContentView.defaultTextColor)
	v.detailView.textColor(TitleContentView.defaultTextColor)
	v.block()
	return v
}