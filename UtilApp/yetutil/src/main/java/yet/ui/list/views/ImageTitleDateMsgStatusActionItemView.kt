package yet.ui.list.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.appbase.ex.RGB
import dev.entao.base.MyDate
import dev.entao.appbase.ex.Colors
import yet.theme.IconSize
import yet.ui.ext.*
import yet.ui.res.D
import yet.ui.res.Shapes
import yet.ui.viewcreator.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

/**
 * --------------------------------------------------------
 * |     | title                          detail |        |
 * |icon |                                       |iconTail|
 * |     | msg                            status |        |
 * --------------------------------------------------------
 */
class ImageTitleDateMsgStatusActionItemView(context: Context) : HorItemView(context) {
	interface ImageItemActionCallback {
		fun onImageItemAction(itemView: ImageTitleDateMsgStatusActionItemView, position: Int)
	}

	var iconView: ImageView = createImageView()
	var titleView: TextView = createTextViewB().singleLine().ellipsizeEnd()
	var dateView: TextView = createTextViewC().singleLine()
	var msgView: TextView = createTextViewC().singleLine().ellipsizeEnd()
	var statusView: TextView = createTextViewC().singleLine()
	var subIconView: ImageView
	var position = -1

	private var callback: ImageItemActionCallback? = null

	var topView: LinearLayout = createLinearHorizontal()
	var bottomView: LinearLayout = createLinearHorizontal()

	init {
		padding(10, 0, 0, 0)
		addViewParam(iconView) { size(IconSize.Small).gravityCenter().margins(0, 0, 5, 0) }
		topView.gravityCenterVertical().apply {
			addViewParam(titleView) { widthDp(0).heightWrap().weight(1f) }
			addViewParam(dateView) { wrap().margins(5, 0, 0, 0) }
		}
		bottomView.gravityCenterVertical().apply {
			addViewParam(msgView) { widthDp(0).heightWrap().weight(1f) }
			addViewParam(statusView) { wrap().margins(5, 0, 0, 0) }
		}
		val ll = createLinearVertical().apply {
			addViewParam(topView) { widthFill().heightWrap() }
			addViewParam(bottomView) { widthFill().heightWrap() }
		}

		this.addViewParam(ll) { widthDp(0).weight(1f).heightWrap().margins(5, 10, 5, 5) }

		subIconView = createImageView().backColor(Color.TRANSPARENT, Colors.Fade).padding(16)
		subIconView.setImageDrawable(arrowRight)
		subIconView.scaleType = ImageView.ScaleType.CENTER_INSIDE
		addViewParam(subIconView) { heightFill().widthDp(45).gravityCenter() }
		subIconView.setOnClickListener(clickListener)
	}

	fun setVerSpace(n: Int) {
		val p = bottomView.layoutParams as MarginLayoutParams
		p.margins(0, n, 0, 0)
		bottomView.layoutParams = p
	}

	fun icon(@DrawableRes resId: Int): ImageTitleDateMsgStatusActionItemView {
		iconView.setImageResource(resId)
		return this
	}

	fun title(title: String): ImageTitleDateMsgStatusActionItemView {
		titleView.text = title
		return this
	}

	fun msg(msg: String): ImageTitleDateMsgStatusActionItemView {
		msgView.text = msg
		return this
	}

	fun status(status: String): ImageTitleDateMsgStatusActionItemView {
		statusView.text = status
		return this
	}

	fun subIcon(@DrawableRes subIconResId: Int): ImageTitleDateMsgStatusActionItemView {
		subIconView.setImageResource(subIconResId)
		return this
	}

	fun date(date: String): ImageTitleDateMsgStatusActionItemView {
		dateView.text = date
		return this
	}

	fun date(date: Long): ImageTitleDateMsgStatusActionItemView {
		dateView.text = MyDate(date).formatShort()
		return this
	}

	fun position(pos: Int): ImageTitleDateMsgStatusActionItemView {
		this.position = pos
		subIconView.tag = this
		return this
	}

	fun setValues(icon: Drawable, title: String, date: String, msg: String, status: String, position: Int): ImageTitleDateMsgStatusActionItemView {
		return setValues(icon, title, date, msg, status, arrowRight, position)
	}

	fun setValues(icon: Drawable, title: String, date: String, msg: String, status: String, subIcon: Drawable?, position: Int): ImageTitleDateMsgStatusActionItemView {
		iconView.setImageDrawable(icon)
		titleView.text = title
		dateView.text = date
		msgView.text = msg
		statusView.text = status
		subIconView.setImageDrawable(subIcon)
		this.position = position
		subIconView.tag = this

		return this
	}

	fun setValues(icon: Drawable, title: String, date: Long, msg: String, status: String, subIcon: Drawable?, position: Int): ImageTitleDateMsgStatusActionItemView {
		val s = if (date == 0L) "" else MyDate(date).formatShort()
		return setValues(icon, title, s, msg, status, subIcon, position)
	}

	fun setValues(icon: Drawable, title: String, date: Long, msg: String, status: String, position: Int): ImageTitleDateMsgStatusActionItemView {
		return setValues(icon, title, date, msg, status, arrowRight, position)
	}

	fun showRedPoint(show: Boolean): ImageTitleDateMsgStatusActionItemView {
		statusView.setCompoundDrawables(null, null, if (show) redDrawable else null, null)
		return this
	}

	fun setCallback(callback: ImageItemActionCallback): ImageTitleDateMsgStatusActionItemView {
		this.callback = callback
		return this
	}

	fun setIconSize(dp: Int): ImageTitleDateMsgStatusActionItemView {
		val p = iconView.layoutParams as LinearLayout.LayoutParams
		p.size(dp)
		iconView.layoutParams = p
		return this
	}

	fun hideAction(hide: Boolean): ImageTitleDateMsgStatusActionItemView {
		subIconView.visibility = if (hide) View.GONE else View.VISIBLE
		return this
	}


	fun enableSubIconClick(enable: Boolean) {
		if (enable) {
			subIconView.setOnClickListener(clickListener)
		} else {
			subIconView.setOnClickListener(null)
			subIconView.isClickable = false
		}
	}

	companion object {

		private val arrowRight = D.ArrowRight

		private val redDrawable = Shapes.oval {
			size(10)
			fillColor = RGB(255, 128, 0)
		}

		private val clickListener = View.OnClickListener { v ->
			val iv = v.tag as? ImageTitleDateMsgStatusActionItemView
			if (iv != null) {
				if (iv.callback != null) {
					iv.callback!!.onImageItemAction(iv, iv.position)
				}
			}

		}
	}
}
