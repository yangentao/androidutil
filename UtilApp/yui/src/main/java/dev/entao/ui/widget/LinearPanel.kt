package dev.entao.ui.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import dev.entao.appbase.ex.Colors
import dev.entao.ui.ext.*
/**
 * Created by entaoyang@163.com on 16/3/13.
 */
/**
 * 类似ListView, 适合小数据.
 * 分割线的颜色就是背景色
 */
open class LinearPanel(context: Context) : LinearLayout(context) {
    interface LinearPanelItemListener {
        fun onItemViewClick(view: View, position: Int)
    }

    private var normalColor = Color.WHITE
    private var pressedColor = Colors.Fade
    private var itemHeight = LinearLayout.LayoutParams.WRAP_CONTENT
    private var divider = 1
    private var itemClickListener: LinearPanelItemListener? = null

    var onItemClick: (View, Int) -> Unit = { _, _ ->

    }
    var onItemClickPos: (Int) -> Unit = { _ ->

    }

    internal var onItemViewClickListener: View.OnClickListener = View.OnClickListener { v ->
        val pos = indexOfChild(v)
        if (pos >= 0) {
            onItemClick.invoke(v, pos)
            onItemClickPos.invoke(pos)
            itemClickListener?.onItemViewClick(v, pos)
        }
    }

    init {
        vertical().backColorPage()
    }

    fun setDivider(nDp: Int): LinearPanel {
        this.divider = nDp
        return this
    }

    fun addItemView(view: View) {
        val first = childCount == 0
        addItemView(view, if (first) 0 else divider)
    }

    fun addItemView(view: View, marginTop: Int, marginBottom: Int = 0) {
        view.makeClickable().backColor(normalColor, pressedColor)
        view.setOnClickListener(onItemViewClickListener)
        if (view.layoutParams == null) {
            addViewParam(view) { widthFill().heightDp(itemHeight).margins(0, marginTop, 0, marginBottom).gravityLeftCenter() }
        } else {
            addView(view)
        }
    }

    fun clean() {
        removeAllViews()
    }

    fun setItemClickListener(listener: LinearPanelItemListener) {
        this.itemClickListener = listener
    }

    fun setItemHeight(dp: Int) {
        itemHeight = dp
    }

    fun setItemHeightWrap() {
        itemHeight = LinearLayout.LayoutParams.WRAP_CONTENT
    }

    fun setItemSelector(normal: Int, pressed: Int) {
        this.normalColor = normal
        this.pressedColor = pressed
    }

}
