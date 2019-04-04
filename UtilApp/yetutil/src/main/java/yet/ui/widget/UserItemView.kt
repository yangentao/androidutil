package yet.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import yet.ui.ext.CenterVertical
import yet.ui.ext.ParentLeft
import yet.ui.ext.ParentTop
import yet.ui.ext.RParam
import yet.ui.ext.below
import yet.ui.ext.marginRight
import yet.ui.ext.margins
import yet.ui.ext.parentRight
import yet.ui.ext.scaleCenterCrop
import yet.ui.ext.size
import yet.ui.ext.textColorMajor
import yet.ui.ext.textColorMinor
import yet.ui.ext.textSizeA
import yet.ui.ext.textSizeB
import yet.ui.ext.toRightOf
import yet.ui.ext.wrap
import yet.ui.res.Res
import yet.ui.viewcreator.imageView
import yet.ui.viewcreator.textView


class UserItemView(context: Context) : RelativeLayout(context) {

	val portraitView: ImageView
	val nameView: TextView
	val statusView: TextView

	init {
		portraitView = imageView(RParam.ParentLeft.CenterVertical.size(64).margins(15)) {
			scaleCenterCrop()
			setImageResource(Res.portrait)
		}

		nameView = textView(RParam.toRightOf(portraitView).ParentTop.wrap().margins(0, 20, 0, 10)) {
			textSizeA()
			textColorMajor()
		}
		statusView = textView(RParam.toRightOf(portraitView).below(nameView).wrap()) {
			textSizeB()
			textColorMinor()
		}

		imageView(RParam.parentRight().CenterVertical.size(14).marginRight(10)) {
			setImageResource(Res.more)
		}
	}

	fun bindValues(name: String, status: String) {
		this.nameView.text = name
		this.statusView.text = status
	}

	fun portrait(resId: Int) {
		portraitView.setImageResource(resId)
	}

	fun portrait(d: Drawable) {
		portraitView.setImageDrawable(d)
	}
}