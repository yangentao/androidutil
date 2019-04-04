package yet.ui.list.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import yet.ui.ext.*
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createTextView

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


class TextDetailItemViewVertical(context: Context) : HorItemView(context) {
    private val verLayout: LinearLayout
    var textView: TextView
    var detailView: TextView

    init {
        verLayout = createLinearVertical()

        textView = createTextView().textSizeA().textColorMajor()
        detailView = createTextView().textSizeB().textColorMinor().singleLine()

        verLayout.addViewParam(textView) { widthFill().heightWrap().gravityLeftCenter() }
        verLayout.addViewParam(detailView) { widthFill().heightWrap().gravityLeftCenter() }
        addViewParam(verLayout) { widthDp(0).weight(1f).heightWrap().gravityLeftCenter() }
    }

    fun setValues(text: String, detail: String?): TextDetailItemViewVertical {
        textView.text = text
        detailView.text = detail
        detailView.visibility = if (detail == null) View.GONE else View.VISIBLE
        return this
    }

    fun textSize(textSp: Int, detailSp: Int): TextDetailItemViewVertical {
        textView.textSizeSp(textSp)
        detailView.textSizeSp(detailSp)
        return this
    }

    fun textColor(textColor: Int, detailColor: Int): TextDetailItemViewVertical {
        textView.setTextColor(textColor)
        detailView.setTextColor(detailColor)
        return this
    }

    companion object {

        fun create(context: Context): TextDetailItemViewVertical {
            return TextDetailItemViewVertical(context)
        }
    }
}