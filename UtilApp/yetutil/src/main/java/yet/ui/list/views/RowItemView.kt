package yet.ui.list.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.appbase.ex.Colors
import yet.theme.TextSize
import yet.ui.ext.*

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

class RowItemView(context: Context) : LinearLayout(context) {
    private val mid: View
    private var space = 10
    private var minWidth = 60
    private var textColor = Colors.TextColorMinor
    private var textSize = TextSize.Normal

    init {
        genId()
        horizontal().padding(10, 10, 10, 10).gravityCenterVertical()
        mid = View(context)
        addViewParam(mid) { widthDp(0).weight(1f).heightDp(10) }
    }

    fun space(spaceDp: Int): RowItemView {
        this.space = spaceDp
        return this
    }

    fun textMinWidth(minWidth: Int): RowItemView {
        this.minWidth = minWidth
        return this
    }

    fun textColor(color: Int): RowItemView {
        this.textColor = color
        return this
    }

    fun textSize(textSize: Int): RowItemView {
        this.textSize = textSize
        return this
    }

    fun getLeft(index: Int): TextView? {
        if (index >= 0 && index < indexOfChild(mid)) {
            return getChildAt(index) as TextView
        }
        return null
    }

    fun getRight(index: Int): TextView? {
        var index2 = index
        if (index2 >= 0) {
            index2 += indexOfChild(mid) + 1
            if (index2 < childCount) {
                return getChildAt(index2) as TextView
            }
        }
        return null
    }

    @JvmOverloads fun addLeft(text: String? = null, textSizeSp: Int = textSize, textColor: Int = this.textColor, minWidth: Int = this.minWidth, marginRight: Int = space): RowItemView {
        val tv = TextView(context)
        tv.textSizeSp(textSizeSp).textColor(textColor).miniWidthDp(minWidth).gravityLeftCenter().text(text)
        addView(tv, indexOfChild(mid), linearParam().wrap().margins(0, 0, marginRight, 0))
        return this
    }

    @JvmOverloads fun addRight(text: String? = null, textSizeSp: Int = textSize, textColor: Int = this.textColor, minWidth: Int = this.minWidth, marginLeft: Int = space): RowItemView {
        val tv = TextView(context)
        tv.textSizeSp(textSizeSp).textColor(textColor).miniWidthDp(minWidth).gravityRightCenter().text(text)
        addView(tv, linearParam().wrap().margins(marginLeft, 0, 0, 0))
        return this
    }

    fun leftText(index: Int, text: String): RowItemView {
        getLeft(index)!!.text = text
        return this
    }

    fun rightText(index: Int, text: String): RowItemView {
        getRight(index)!!.text = text
        return this
    }

    fun leftText(index: Int): String {
        return getLeft(index)!!.text.toString()
    }

    fun rightText(index: Int): String {
        return getRight(index)!!.text.toString()
    }

    fun leftText(vararg texts: String): RowItemView {
        for (i in texts.indices) {
            leftText(i, texts[i])
        }
        return this
    }

    fun rightText(vararg texts: String): RowItemView {
        for (i in texts.indices) {
            rightText(i, texts[i])
        }
        return this
    }
}
