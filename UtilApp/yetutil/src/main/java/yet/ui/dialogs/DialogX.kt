@file:Suppress("unused")

package yet.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.CardView
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import yet.theme.Colors
import yet.theme.ViewSize
import yet.ui.ext.*
import yet.ui.grid.SimpleGridView
import yet.ui.list.CheckListView
import yet.ui.list.SimpleListView
import yet.ui.viewcreator.*
import yet.ui.widget.TitleBar
import yet.util.Task
import yet.util.app.App

class DialogX(val context: Context) {
    private var titleHeight = 45
    val dlg = Dialog(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar)
    val dlgParam: WindowManager.LayoutParams = dlg.window?.attributes!!
    private val rootLayout = context.createLinearVertical()
    val cardView = CardView(context)
    var bodyView: View = context.createTextViewB().textColorMajor()
    var bodyViewParam: LinearLayout.LayoutParams = LParam.WidthFill.HeightWrap
    var onDismiss: (DialogX) -> Unit = {}
    var beforeDismiss: (DialogX) -> Unit = {}


    private var buttons = ArrayList<DialogButton>()
    var title: String = ""
    val result = ArrayList<Any>()

    var argS: String = ""
    var argN = 0

    init {
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dlg.setCancelable(true)
        dlg.setCanceledOnTouchOutside(true)
        dlg.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dlg.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dlg.window?.setDimAmount(0.5f)
        dlgParam.widthFill()
        dlgParam.heightWrap()

        cardView.setCardBackgroundColor(Color.WHITE)
        cardView.cardElevation = dp(5).toFloat()
        cardView.preventCornerOverlap = false
        cardView.radius = dp(ViewSize.DialogCorner).toFloat()
        cardView.minimumHeight = dp(80)
//        cardView.minimumWidth = dp(200)
        cardView.minimumWidth = App.screenWidthPx * 2 / 4

        cardView.addView(rootLayout, FParam.WidthFill.HeightWrap)
        rootLayout.divider()
    }


    fun title(title: String?) {
        this.title = title ?: ""
    }

    fun button(text: String, color: Int, block: (String) -> Unit): DialogButton {
        val b = DialogButton(text, color, block)
        buttons.add(b)
        return b
    }

    fun risk(text: String, block: (String) -> Unit) {
        button(text, Colors.Risk, block)
    }

    fun safe(text: String, block: (String) -> Unit) {
        button(text, Colors.GreenMajor, block)
    }

    fun normal(text: String, block: (String) -> Unit) {
        button(text, Colors.TextColorMajor, block)
    }

    fun cancel(text: String = "取消") {
        normal(text) {}
    }

    fun ok(text: String = "确定", block: (String) -> Unit) {
        safe(text, block)
    }

    fun buttons(block: DialogX.() -> Unit) {
        this.block()
    }


    fun body(view: View) {
        this.bodyView = view
    }

    fun body(block: DialogX.() -> View) {
        this.bodyView = this.block()
    }

    fun bodyText(text: String, block: TextView.() -> Unit = {}) {
        val tv = context.createTextViewB()
        tv.padding(15, 10, 15, 10)
        tv.text = text
        tv.gravityCenter()
        tv.linkifyAll()
        tv.block()
        body(tv)
    }

    fun bodyInput(block: EditText.() -> Unit) {
        val rl = context.createRelative()
        val ed = rl.editX(RParam.CenterInParent.WidthFill.HeightWrap.margins(15)) {
            minimumWidth = dp(200)
            minimumHeight = dp(ViewSize.EditHeight)
            this.block()
        }
        beforeDismiss = {
            result.add(ed.textS)
        }
        body(rl)
    }

    fun bodyInputLines(block: EditText.() -> Unit) {
        val rl = context.createRelative()
        val ed = rl.edit(RParam.CenterInParent.WidthFill.HeightWrap.margins(15)) {
            minimumWidth = dp(200)
            minimumHeight = dp(ViewSize.EditHeight * 5)
            this.multiLine()
            minLines = 5
            gravityTopLeft()
            padding(5)
            this.block()
        }
        beforeDismiss = {
            result.add(ed.textS)
        }
        body(rl)
    }

    fun bodyList(block: SimpleListView.() -> Unit): SimpleListView {
        bodyViewParam = LParam.WidthFill.HeightFlex
        val lv = SimpleListView(context)
        body(lv)
        lv.block()
        return lv
    }

    fun bodyListString(block: (Any) -> String = { it.toString() }): SimpleListView {
        return bodyList {
            anyAdapter.onBindView = { v, p ->
                (v as TextView).text = block(anyAdapter.item(p))
            }
        }
    }

    fun bodyListCheck(block: CheckListView.() -> Unit): CheckListView {
        bodyViewParam = LParam.WidthFill.HeightFlex
        val lv = CheckListView(context)
        body(lv)
        lv.block()
        return lv
    }

    fun bodyCheckString(block: (Any) -> String = { it.toString() }): CheckListView {
        return bodyListCheck {
            onBindView = { v, p ->
                (v as TextView).text = block(anyAdapter.item(p))
            }
        }
    }

    fun bodyGrid(block: SimpleGridView.() -> Unit): SimpleGridView {
        bodyViewParam = LParam.WidthFill.HeightFlex
        val lv = SimpleGridView(context)
        lv.verticalSpacing = dp(5)
        body(lv)
        lv.block()
        return lv
    }


    private fun createView() {
        if (title.isNotEmpty()) {
            rootLayout.textView(LParam.WidthFill.height(titleHeight)) {
                textColorWhite().textSizeTitle()
                backColorTheme()
                if (TitleBar.TitleCenter) {
                    gravityCenter()
                } else {
                    padding(15, 0, 0, 0)
                    gravityLeftCenter()
                }
                textS = title
            }
        }

        bodyView.minimumHeight = dp(60)
//        bodyView.minimumWidth = dp(240)
        rootLayout.addView(bodyView, bodyViewParam)

        if (buttons.isNotEmpty()) {
            rootLayout.linearHor(LParam.WidthFill.HeightButton) {
                divider()
                for (b in buttons) {
                    textView(LParam.gravityCenter().HeightFill.WidthFlex) {
                        textSizeA()
                        textColor(b.color)
                        backColor(b.backColor)
                        padding(15, 10, 15, 10)
                        gravityCenter()
                        textS = b.text
                        onClick {
                            dismiss()
                            b.callback(b.text)
                        }
                    }
                }
            }
        }
    }


    fun show() {
        createView()
        dlg.setContentView(cardView)
        dlg.setOnDismissListener {
            onDismiss(this)
        }
        dlg.show()
        val tm = timeoutToCloseSeconds
        if (tm > 1) {
            Task.foreDelay(tm * 1000L) {
                if (dlg.isShowing) {
                    dlg.dismiss()
                }
            }
        }
    }


    fun dismiss() {
        beforeDismiss(this)
        dlg.dismiss()
    }

    fun gravityTop(yMargin: Int) {
        dlgParam.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        dlgParam.y = dp(yMargin)
        dlg.window!!.attributes = dlgParam
    }

    class DialogButton(val text: String, val color: Int, val callback: (String) -> Unit) {
        var backColor: Int = Colors.WHITE
    }


    companion object {
        var timeoutToCloseSeconds = 0

        fun show(context: Context, block: DialogX.() -> Unit) {
            val d = DialogX(context)
            d.block()
            d.show()
        }

        fun alert(context: Context, msg: String) {
            alert(context, msg, null)
        }

        fun alert(context: Context, msg: String, title: String?, dismissCallback: () -> Unit = {}) {
            val d = DialogX(context)
            d.title(title)
            d.bodyText(msg) {
                if (msg.length < 16) {
                    gravityCenter()
                } else {
                    gravityLeftCenter()
                }
            }
            d.ok {}
            d.onDismiss = {
                dismissCallback()
            }
            d.show()
        }

        fun confirm(context: Context, msg: String, title: String?, onOK: () -> Unit) {
            val d = DialogX(context)
            d.title(title)
            d.bodyText(msg)
            d.cancel()
            d.ok {
                onOK()
            }
            d.show()
        }

        fun input(context: Context, title: String?, value: String = "", onOK: (String) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            d.bodyInput {
                this.textS = value
            }
            d.cancel()
            d.ok {
                val s = d.result.first() as String
                onOK(s)
            }
            d.show()
        }

        fun inputLines(context: Context, title: String?, value: String = "", onOK: (String) -> Unit) {
            val d = DialogX(context)
            d.title(title)
            d.bodyInputLines {
                this.textS = value
            }
            d.cancel()
            d.ok {
                val s = d.result.first() as String
                onOK(s)
            }
            d.show()
        }


        fun listItem(
            context: Context, items: List<Any>, title: String?,
            textBlock: (Any) -> String, onResult: (Any) -> Unit
        ) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyListString(textBlock)
            lv.setItems(items)
            lv.onItemClick = {
                d.dismiss()
                onResult(it)
            }
            d.show()
        }

        fun listItemN(
            context: Context, items: List<Any>, title: String?,
            textBlock: (Any) -> String, onResult: (Int) -> Unit
        ) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyListString(textBlock)
            lv.setItems(items)
            lv.onItemClick2 = { _, _, p ->
                d.dismiss()
                onResult(p)
            }
            d.show()
        }

        fun listString(context: Context, items: List<String>, title: String? = "选择", onResult: (String) -> Unit) {
            listItem(context, items, title, { it.toString() }) {
                onResult(it as String)
            }
        }


        fun listStringN(context: Context, items: List<String>, title: String? = "选择", onResult: (Int) -> Unit) {
            listItemN(context, items, title, { it.toString() }, onResult)
        }

        fun checkItems(
            context: Context, items: List<Any>, checkedItems: List<Any>, title: String,
            textBlock: (Any) -> String, onResult: (List<Any>) -> Unit
        ) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyCheckString(textBlock)
            lv.setItems(items)
            for (item in checkedItems) {
                lv.anyAdapter.checkItem(item)
            }
            d.cancel()
            d.ok {
                onResult(lv.anyAdapter.checkedItems)
            }
            d.show()
        }

        fun checkItemsN(
            context: Context, items: List<Any>, checkedItems: List<Any>, title: String,
            textBlock: (Any) -> String, onResult: (Set<Int>) -> Unit
        ) {
            val d = DialogX(context)
            d.title(title)
            val lv = d.bodyCheckString(textBlock)
            lv.setItems(items)
            for (item in checkedItems) {
                lv.anyAdapter.checkItem(item)
            }
            d.cancel()
            d.ok {
                onResult(lv.anyAdapter.checkedIndexs)
            }
            d.show()
        }

        fun checkString(
            context: Context, items: List<String>, checkedItems: List<Any>,
            title: String = "选择", onResult: (List<String>) -> Unit
        ) {
            checkItems(context, items, checkedItems, title, { it.toString() }) {
                onResult(it.map { it as String })
            }
        }


        fun checkStringN(
            context: Context, items: List<String>, checkedItems: List<Any>,
            title: String = "选择", onResult: (Set<Int>) -> Unit
        ) {
            checkItemsN(context, items, checkedItems, title, { it.toString() }, onResult)
        }

        fun selectGrid(context: Context, items: List<Any>, callback: GridConfig.() -> Unit) {
            val d = DialogX(context)
            d.bodyGrid {
                val c = GridConfig(d, this)
                c.callback()
                setItems(items)
                onItemClick = {
                    d.dismiss()
                    c.onResult.invoke(it)
                }
            }
            d.show()
        }

    }

}

class GridConfig(val dialog: DialogX, val grid: SimpleGridView) {
    var onResult: (Any) -> Unit = {}
}
