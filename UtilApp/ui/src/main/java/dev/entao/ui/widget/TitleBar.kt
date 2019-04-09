package dev.entao.ui.widget

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.view.ViewGroup
import android.widget.*
import dev.entao.appbase.ex.*

import dev.entao.ui.ext.*
import dev.entao.ui.res.D
import dev.entao.ui.res.Res
import dev.entao.ui.viewcreator.*
import dev.entao.util.Task

class TitleBar(val context: Activity) : RelativeLayout(context) {
    private val leftCmds = ArrayList<BarItem>()
    private val rightCmds = ArrayList<BarItem>()
    var titleView: View? = null
    var titleCenter = TitleBar.TitleCenter

    var leftLinear: LinearLayout? = null
    var rightLinear: LinearLayout? = null

    var pushModel: Boolean = false
        private set
    private var leftBack = ArrayList<BarItem>()
    private var rightBack = ArrayList<BarItem>()
    private var titleBack: View? = null

    var popWindow: PopupWindow? = null

    private var titleClickCallback: () -> Unit = {}

    init {
        backColor(Colors.Theme)
    }

    fun push(block: TitleBar.() -> Unit) {
        if (!pushModel) {
            pushModel = true

            moveTo(leftCmds, leftBack)
            moveTo(rightCmds, rightBack)

            titleBack = titleView
            titleView = null
            for (a in leftBack) {
                a.view?.removeFromParent()
            }
            for (a in rightBack) {
                a.view?.removeFromParent()
            }

            this.block()
            commit()
        }
    }

    fun pop() {
        if (pushModel) {
            pushModel = false
            moveTo(leftBack, leftCmds)
            moveTo(rightBack, rightCmds)
            titleView = titleBack
            titleBack = null

            commit()
        }
    }

    private fun moveTo(from: ArrayList<BarItem>, dest: ArrayList<BarItem>) {
        dest.clear()
        dest.addAll(from)
        from.clear()
    }

    fun findMenuItem(cmd: String): BarItem? {
        return find {
            it.cmd == cmd
        }
    }

    fun find(block: (BarItem) -> Boolean): BarItem? {
        for (a in leftCmds) {
            if (block(a)) {
                return a
            }
            for (b in a.children) {
                if (block(b)) {
                    return b
                }
            }
        }
        for (a in rightCmds) {
            if (block(a)) {
                return a
            }
            for (b in a.children) {
                if (block(b)) {
                    return b
                }
            }
        }
        return null
    }

    fun find(cmd: String): BarItem? {
        return find {
            it.cmd == cmd
        }
    }


    private fun makeSureView(item: BarItem) {
        if (item.view != null) {
            item.view?.removeFromParent()
            return
        }
        var d: Drawable? = null
        if (item.drawable != null) {
            d = item.drawable?.mutate()
        } else if (item.resIcon != 0) {
            d = Res.drawable(item.resIcon).mutate()
        }
        if (d != null) {
            if (item.tintTheme) {
                d = d.tintedWhite
            }
            d = d.sized(TitleBar.ImgSize)
            val iv = ImageView(context)
            iv.scaleCenterCrop()
            iv.backColorTransFade()
            iv.padding(PAD_HOR, PAD_VER, PAD_HOR, PAD_VER)
            iv.setImageDrawable(d)
            item.view = iv
            item.param = LParam.widthWrap().heightFill()
            return
        }
        val tv = createTextViewB()
        tv.backColorTransFade()
        tv.textColorWhite()
        tv.gravityCenter()
        tv.minimumWidth = dp(HEIGHT)
        tv.padding(5, 0, 5, 0)
        tv.text = item.text
        item.view = tv
        item.param = LParam.WidthWrap.HeightFill.gravityCenter()
    }

    fun commit() {
        leftLinear?.removeAllViews()
        rightLinear?.removeAllViews()
        removeAllViews()

        if (leftCmds.isNotEmpty()) {
            val ll = this.createLinearHorizontal()
            for (c in leftCmds.filter { !it.hidden }) {
                makeSureView(c)
                ll.addView(c.view, c.param)
                if (c.children.isEmpty()) {
                    c.view!!.setOnClickListener {
                        c.onClick(c.cmd)
                    }
                } else {
                    c.view!!.setOnClickListener {
                        popMenu(c)
                    }
                }
            }
            addView(ll, RParam.ParentLeft.HeightFill.WidthWrap)
            leftLinear = ll
        }

        if (rightCmds.isNotEmpty()) {
            val ll = this.createLinearHorizontal()
            for (c in rightCmds.filter { !it.hidden }) {
                makeSureView(c)
                ll.addView(c.view, c.param)
                if (c.children.isEmpty()) {
                    c.view!!.setOnClickListener {
                        c.onClick(c.cmd)
                    }
                } else {
                    c.view!!.setOnClickListener {
                        popMenu(c)
                    }
                }
            }
            addView(ll, RParam.ParentRight.HeightFill.WidthWrap)
            rightLinear = ll
        }
        val v = titleView
        if (v != null) {
            if (titleCenter) {
                addView(v, RParam.CenterInParent.HeightFill.WidthWrap)
            } else {
                if (leftLinear == null) {
                    addView(v, RParam.CenterVertical.ParentLeft.HeightFill.WidthWrap.margins(15, 0, 0, 0))
                } else {
                    addView(v, RParam.CenterVertical.toRightOf(leftLinear!!).HeightFill.WidthWrap.margins(15, 0, 0, 0))
                }
            }
        }

    }

    fun title(text: String): TextView {
        val tv = createTextViewA()
        tv.textColorWhite()
        tv.text = text
        titleView = tv
        titleView?.onClick {
            titleClickCallback()
        }
        return tv
    }

    fun titleImage(@DrawableRes resId: Int): ImageView {
        val iv = createImageView()
        iv.setImageResource(resId)
        iv.scaleCenterCrop()
        titleView = iv
        titleView?.onClick {
            titleClickCallback()
        }
        return iv
    }

    fun onTitleClick(block: () -> Unit) {
        titleClickCallback = block

    }

    fun removeBack() {
        removeCmd(BACK)
    }

    fun removeCmd(cmd: String) {
        val c = find(cmd)
        c?.view?.removeFromParent()

        leftCmds.removeAll { it.cmd == cmd }
        for (a in leftCmds) {
            a.children.removeAll { it.cmd == cmd }
        }
        rightCmds.removeAll { it.cmd == cmd }
        for (a in rightCmds) {
            a.children.removeAll { it.cmd == cmd }
        }
    }


    fun showBack(resId: Int = Res.back): BarItem {
        val c = find(BACK)
        if (c != null) {
            return c
        }
        val a = leftImage(resId, BACK)
        a.onClick = {
            context.finish()
        }
        return a
    }

    fun rightImage(resId: Int, BarItem: String = "$resId"): BarItem {
        return rightImage(Res.drawable(resId), BarItem)
    }

    fun rightImage(d: Drawable, cmd: String = BarItem.autoIdent()): BarItem {
        val c = BarItem(cmd)
        c.drawable = d
        rightCmds.add(c)
        return c
    }

    fun rightText(text: String, cmd: String = text): BarItem {
        val c = BarItem(cmd)
        c.text = text
        rightCmds.add(c)
        return c
    }

    fun leftImage(resId: Int, BarItem: String = "$resId"): BarItem {
        return leftImage(Res.drawable(resId), BarItem)
    }

    fun leftImage(d: Drawable, cmd: String = BarItem.autoIdent()): BarItem {
        val c = BarItem(cmd)
        c.drawable = d
        leftCmds.add(c)
        return c
    }

    fun leftText(text: String, cmd: String = text): BarItem {
        val c = BarItem(cmd)
        c.text = text
        leftCmds.add(c)
        return c
    }

    private fun popMenu(item: BarItem) {
        val p = PopupWindow(context)
        p.width = ViewGroup.LayoutParams.WRAP_CONTENT
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT
        p.isFocusable = true
        p.isOutsideTouchable = true
        p.setBackgroundDrawable(ColorDrawable(0))
        val gd = Shapes.rect {
            fillColor = Colors.Theme
            cornerListDp(0, 0, 2, 2)
        }
        val popRootView = context.createLinearVertical()
        popRootView.minimumWidth = dp(150)
        popRootView.backDrawable(gd).padding(5)
        popRootView.divider()
        val itemList = ArrayList<BarItem>(item.children.filter { !it.hidden })
        for (c in itemList) {
            val v = makeSureMenuItemView(c)
            popRootView.addView(v, LParam.WidthFill.height(45))
            v.setOnClickListener {
                popWindow?.dismiss()
                Task.fore {
                    c.onClick(c.cmd)
                }
            }
        }
        p.contentView = popRootView
        popWindow = p

        p.setOnDismissListener {
            (popWindow?.contentView as? ViewGroup)?.removeAllViews()
            popWindow = null
        }
        p.showAsDropDown(item.view, 0, 1)
    }

    fun menu(block: BarItem.() -> Unit) {
        val m = find(MENU) ?: rightImage(Res.menu, MENU)
        m.block()
    }

    fun menu(resId: Int, block: BarItem.() -> Unit) {
        val m = find(MENU) ?: rightImage(resId, MENU)
        m.block()
    }

    private fun makeSureMenuItemView(item: BarItem): View {
        if (item.view != null) {
            item.view?.removeFromParent()
            return item.view!!
        }
        val tv = context.createTextViewB()
        tv.singleLine()
        tv.backColorTransFade()
        tv.gravityLeftCenter().padding(5, 5, 20, 5)
        tv.text = item.text

        var d: Drawable = if (item.drawable != null) {
            item.drawable!!.mutate()
        } else if (item.resIcon != 0) {
            Res.drawable(item.resIcon).mutate()
        } else {
            D.color(Color.TRANSPARENT)
        }
        d = if (item.tintTheme) {
            d.tintedWhite.sized(TitleBar.ImgSize)
        } else {
            d.sized(TitleBar.ImgSize)
        }
        tv.compoundDrawablePadding = dp(10)
        tv.setCompoundDrawables(d, null, null, null)
        tv.textColorWhite()
        item.view = tv
        return tv
    }


    companion object {
        const val BACK = "back"
        const val MENU = "menu"
        const val ImgSize = 24
        const val HEIGHT = 50// dp
        const val PAD_VER = (HEIGHT - ImgSize) / 2
        const val PAD_HOR = (HEIGHT - ImgSize) / 2
        var TitleCenter = true
    }
}