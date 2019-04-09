package dev.entao.ui.widget

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.appbase.ex.Colors
import dev.entao.ui.ext.*
import dev.entao.ui.viewcreator.createTextViewB

/**
 * Created by entaoyang@163.com on 2016-09-04.
 */

class SwitchItemView(context: Context) : LinearLayout(context) {
	var textView: TextView

	init {
		this.genId()
		this.orientationVertical().backColor(Colors.WHITE, Colors.Fade)
		textView = context.createTextViewB()
		textView.padding(20, 10, 20, 10).gravityCenter().backColor(Colors.WHITE)
		this.addViewParam(textView) { widthFill().heightFill().margins(0, 0, 0, 3) }
	}

	var text: String
		get() {
			return textView.text.toString()
		}
		set(text) {
			textView.text(text)
		}

}