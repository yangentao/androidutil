package yet.ui.list.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.base.MyDate
import yet.ext.RGB
import yet.theme.IconSize
import yet.ui.ext.*
import yet.ui.res.Shapes
import yet.ui.viewcreator.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

/**
 * -----------------------------------------------
 * |     | title                          detail |
 * |icon |                                       |
 * |     | msg                            status |
 * -----------------------------------------------
 */
class ImageTitleDateMsgStatusItemView(context: Context) : HorItemView(context) {
	var iconView: ImageView = createImageView()
	var titleView: TextView = createTextViewB().singleLine().ellipsizeEnd()
	var dateView: TextView = createTextViewC().singleLine()
	var msgView: TextView = createTextViewC().singleLine().ellipsizeEnd()
	var statusView: TextView = createTextViewC().singleLine()

	var topView = createLinearHorizontal().gravityCenterVertical()
	var bottomView = createLinearHorizontal().gravityCenterVertical()

	var verticalLayout = createLinearVertical()

	init {
		padding(10, 0, 10, 0)
		iconView = imageView(lParam().size(IconSize.Small).gravityCenter().margins(0, 0, 5, 0)) {
			scaleCenterCrop()
		}

		verticalLayout.apply {
			topView.apply {
				addViewParam(titleView) { widthDp(0).heightWrap().weight(1f) }
				addView(dateView, linearParam().wrap().margins(5, 0, 0, 0))
			}
			addViewParam(topView) { widthFill().heightWrap() }
			bottomView.apply {
				addViewParam(msgView) { widthDp(0).heightWrap().weight(1f) }
				addViewParam(statusView) { wrap().margins(5, 0, 0, 0) }
			}
			addViewParam(bottomView) { widthFill().heightWrap() }
		}
		addViewParam(verticalLayout) { widthDp(0).weight(1f).heightWrap().margins(5, 10, 5, 5).gravityCenterVertical() }
	}

	fun setVerSpace(n: Int) {
		val p = bottomView.layoutParams as MarginLayoutParams
		p.margins(0, n, 0, 0)
		bottomView.layoutParams = p
	}

	fun setSepratorSize(dp: Int) {
		val p = topView.layoutParams as LinearLayout.LayoutParams
		p.margins(p.leftMargin, p.topMargin, p.rightMargin, dp)
		topView.layoutParams = p
	}

	fun setIconSize(dp: Int): ImageTitleDateMsgStatusItemView {
		val p = iconView.layoutParams as LinearLayout.LayoutParams
		p.size(dp)
		iconView.layoutParams = p
		return this
	}

	fun icon(@DrawableRes resId: Int): ImageTitleDateMsgStatusItemView {
		iconView.setImageResource(resId)
		return this
	}

	fun icon(d: Drawable): ImageTitleDateMsgStatusItemView {
		iconView.setImageDrawable(d)
		return this
	}

	fun title(title: String): ImageTitleDateMsgStatusItemView {
		titleView.text = title
		return this
	}

	fun msg(msg: String): ImageTitleDateMsgStatusItemView {
		msgView.text = msg
		return this
	}

	fun date(date: String): ImageTitleDateMsgStatusItemView {
		dateView.text = date
		return this
	}

	fun date(date: Long): ImageTitleDateMsgStatusItemView {
		dateView.text = MyDate(date).formatShort()
		return this
	}

	fun status(status: String): ImageTitleDateMsgStatusItemView {
		statusView.text = status
		return this
	}

	fun setValues(icon: Drawable?, title: String, date: String, msg: String, status: String): ImageTitleDateMsgStatusItemView {
		iconView.setImageDrawable(icon)
		titleView.text = title
		dateView.text = date
		msgView.text = msg
		statusView.text = status
		return this
	}

	fun setValues(icon: Drawable?, title: String, date: Long, msg: String, status: String): ImageTitleDateMsgStatusItemView {
		val s = if (date == 0L) "" else MyDate(date).formatShort()
		return setValues(icon, title, s, msg, status)
	}

	fun setValues(title: String, date: Long, msg: String, status: String): ImageTitleDateMsgStatusItemView {
		val s = if (date == 0L) "" else MyDate(date).formatShort()
		return setValues(title, s, msg, status)
	}

	fun setValues(title: String, date: String, msg: String, status: String): ImageTitleDateMsgStatusItemView {
		titleView.text = title
		dateView.text = date
		msgView.text = msg
		statusView.text = status
		return this
	}

	fun showRedPoint(show: Boolean): ImageTitleDateMsgStatusItemView {
		statusView.setCompoundDrawables(null, null, if (show) redDrawable else null, null)
		return this
	}

	fun msgMultiLines(n: Int = 2) {
		msgView.multiLine()
		msgView.maxLines = n
	}

	fun spaceCenter(n: Int) {
		val lp = topView.layoutParams as ViewGroup.MarginLayoutParams
		lp.bottomMargin = dp(n)
		topView.layoutParams = lp
	}

	companion object {
		private val redDrawable = Shapes.oval {
			size(10)
			fillColor = RGB(255, 128, 0)
		}
	}

}
