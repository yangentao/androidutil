package yet.ui.list.views

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.ybase.MyDate
import yet.ext.RGB
import yet.ui.ext.*
import yet.ui.res.Shapes
import yet.ui.viewcreator.createLinearHorizontal
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createTextViewB
import yet.ui.viewcreator.createTextViewC

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

// 有3个文本的view, name和date在一行, msg在他们的下面
// name----------------date
// msg-----------------status
class TitleDateMsgStatusItemView(context: Context) : HorItemView(context) {
	private val verLayout: LinearLayout
	var titleView: TextView
	var dateView: TextView
	var msgView: TextView
	var statusView: TextView

	init {
		verLayout = createLinearVertical()
		val top = createLinearHorizontal().gravityCenterVertical()
		titleView = createTextViewB().singleLine().ellipsizeEnd()
		dateView = createTextViewC().singleLine()

		top.addViewParam(titleView) { widthDp(0).heightWrap().weight(1f) }
		top.addViewParam(dateView) { wrap().margins(5, 0, 0, 0) }

		val bottom = createLinearHorizontal().gravityCenterVertical()
		msgView = createTextViewC().singleLine().ellipsizeEnd()
		statusView = createTextViewC().singleLine()
		bottom.addViewParam(msgView) { widthDp(0).heightWrap().weight(1f) }
		bottom.addViewParam(statusView) { wrap().margins(5, 0, 0, 0) }

		verLayout.addViewParam(top) { widthFill().heightWrap() }
		verLayout.addViewParam(bottom) { widthFill().heightWrap().margins(0, 5, 0, 0) }
		this.addViewParam(verLayout) { widthFill().heightWrap() }
	}

	fun title(title: String): TitleDateMsgStatusItemView {
		titleView.text = title
		return this
	}

	fun msg(msg: String): TitleDateMsgStatusItemView {
		msgView.text = msg
		return this
	}

	fun date(date: String): TitleDateMsgStatusItemView {
		dateView.text = date
		return this
	}

	fun date(date: Long): TitleDateMsgStatusItemView {
		dateView.text = MyDate(date).formatShort()
		return this
	}

	fun status(status: String): TitleDateMsgStatusItemView {
		statusView.text = status
		return this
	}

	fun setValues(name: String, date: String, msg: String, status: String?): TitleDateMsgStatusItemView {
		titleView.text = name
		dateView.text = date
		msgView.text = msg
		statusView.text = status
		return this
	}

	fun setValues(name: String, date: Long, msg: String, status: String?): TitleDateMsgStatusItemView {
		val s = if (date == 0L) "" else MyDate(date).formatShort()
		return setValues(name, s, msg, status)
	}

	fun showRedPoint(show: Boolean): TitleDateMsgStatusItemView {
		statusView.setCompoundDrawables(null, null, if (show) redDrawable else null, null)
		return this
	}

	companion object {

		fun create(context: Context): TitleDateMsgStatusItemView {
			return TitleDateMsgStatusItemView(context)
		}

		private val redDrawable = Shapes.oval {
			size(10)
			fillColor = RGB(255, 128, 0)
		}
	}

}
