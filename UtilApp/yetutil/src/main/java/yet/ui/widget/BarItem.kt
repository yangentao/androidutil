package yet.ui.widget

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.LinearLayout

class BarItem(val cmd: String) {
    var text: String = cmd
    var resIcon: Int = 0
    var drawable: Drawable? = null
    var index: Int = -1
    var tintTheme: Boolean = true
    var hidden: Boolean = false
    var view: View? = null
    var param: LinearLayout.LayoutParams? = null
    var onClick: (String) -> Unit = {}

    val children = ArrayList<BarItem>()

    fun add(cmd: String, block: BarItem.() -> Unit): BarItem {
        val item = BarItem(cmd)
        item.block()
        children.add(item)
        return item
    }

    fun add(@DrawableRes resId: Int, text: String, cmd: String = text): BarItem {
        return add(cmd) {
            resIcon = resId
            this.text = text
        }
    }

    companion object {
        private var i = 0
        fun autoIdent(): String {
            i++
            return "BarItem$i"
        }

    }
}