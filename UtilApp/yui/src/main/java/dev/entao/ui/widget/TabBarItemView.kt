package dev.entao.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.RelativeLayout
import android.widget.TextView
import dev.entao.appbase.ex.*
import dev.entao.ui.ext.*
import dev.entao.ui.res.D
import dev.entao.ui.res.Res
import dev.entao.ui.viewcreator.textView

/**
 * Created by entaoyang@163.com on 2018-04-18.
 */

open class TabBarItemView(context: Context) : RelativeLayout(context) {
	val textView: TextView
	val indicateView: TextView
	var autoTintDrawable = true
	var tabIndex: Int = 0

	init {
		genId()
		backColorWhite()
		textView = this.textView(RParam.CenterInParent.Wrap) {
			compoundDrawablePadding = 0
			textSizeD()
			gravityCenter()
			padding(1, 4, 1, 1)
			textColor(D.listColor(Colors.TextColorMajor, Colors.Theme))
		}
		indicateView = textView(RParam.ParentTop.ParentRight.margins(0, 5, 5, 0)) {
			textColor(Color.WHITE)
			textSizeSp(10)
			gravityCenter()

			backDrawable {
				fillColor = RGB(255, 80, 80)
				cornerPx = dp(7)
			}
			padding(2, 0, 2, 0)
			minimumWidth = dp(14)
			gone()
		}
	}

	//0: 不显示; <0 只显示红点; >0显示红点和数字
	fun setIndicate(n: Int) {
		if (n == 0) {
			indicateView.gone()
			return
		}
		if (n > 0) {
			indicateView.text = "$n"
			indicateView.visiable()
			return
		}
		indicateView.text = ""
		indicateView.visiable()
	}

	fun setText(text: String) {
		textView.textS = text
	}

	fun setIcon(res: Int) {
		if (res != 0) {
			setIcon(Res.drawable(res))
		}
	}

	fun setIcon(d: Drawable) {
		val dd = if (autoTintDrawable) {
			d.tinted(Colors.Unselected, Colors.Theme)
		} else {
			d.mutate()
		}
		textView.topImage(dd.sized(24), 0)
	}

	val text: String get() = textView.textS
}