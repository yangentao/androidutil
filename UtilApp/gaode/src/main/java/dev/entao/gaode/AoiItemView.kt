package dev.entao.gaode

import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import dev.entao.ui.ext.*
import dev.entao.ui.viewcreator.imageView
import dev.entao.ui.viewcreator.textView


class AoiItemView(context: Context) : RelativeLayout(context) {
	val nameView: TextView
	val addressView: TextView
	val checkView: ImageView


	init {
		genId()
		padding(15, 10, 15, 10)
		nameView = textView(RParam.ParentLeft.ParentTop.Wrap) {
			text = " "
			textColorMajor()
			textSizeB()
		}
		addressView = textView(RParam.ParentLeft.below(nameView).Wrap.marginTop(5)) {
			text = " "
			textColorMinor()
			textSizeC()
		}
		checkView = imageView(RParam.ParentRight.CenterVertical.size(20)) {

		}
	}
}