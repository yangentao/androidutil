package dev.entao.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import dev.entao.appbase.ex.*
import dev.entao.ui.ext.*
import dev.entao.ui.page.Cmd
import dev.entao.ui.res.D
import dev.entao.ui.res.Res
import dev.entao.ui.viewcreator.createLinearVertical
import dev.entao.ui.viewcreator.createTextViewB
import dev.entao.util.Task
/**
 * Created by entaoyang@163.com on 2018-04-18.
 */

class BottomBar(context: Context) : LinearLayout(context) {
	val cmdList = ArrayList<Cmd>()
	private var cmdBack = ArrayList<Cmd>()
	var pushModel: Boolean = false
		private set

	var autoTintDrawable = true

	var popWindow: PopupWindow? = null

	init {
		genId()
		horizontal()
		backColor(Colors.WHITE)
		divider()
	}

	fun push(block: BottomBar.() -> Unit) {
		if (!pushModel) {
			pushModel = true
			moveTo(cmdList, cmdBack)
			this.block()
			commit()
		}
	}

	fun pop() {
		if (pushModel) {
			pushModel = false
			moveTo(cmdBack, cmdList)
			commit()
		}
	}

	private fun <T> moveTo(from: ArrayList<T>, dest: ArrayList<T>) {
		dest.clear()
		dest.addAll(from)
		from.clear()
	}

	fun findMenuItem(cmd: String): ActionMenuItemInfo? {
		for (c in cmdList) {
			val m = c.items.find { it.cmd == cmd }
			if (m != null) {
				return m
			}
		}
		return null
	}

	fun find(block: (Cmd) -> Boolean): Cmd? {
		return cmdList.find(block)
	}

	fun find(cmd: String): Cmd? {
		return find {
			it.cmd == cmd
		}
	}

	fun commit() {
		removeAllViews()
		for (cmd in cmdList) {
			this.addView(cmd.view,  LParam.WidthFlex.HeightFill)
			if (cmd.items.isEmpty()) {
				cmd.view.setOnClickListener {
					cmd.onClick(cmd)
				}
			} else {
				cmd.view.setOnClickListener {
					popMenu(cmd)
				}
			}
		}
	}

	fun actionImage(resId: Int, cmd: String = "$resId"): Cmd {
		return actionImage(Res.drawable(resId), cmd)
	}

	fun actionImage(d: Drawable, cmd: String = Cmd.genCmd): Cmd {
		val iv = createImageItemView()
		if (autoTintDrawable) {
			iv.setImageDrawable(d.tinted(Colors.TextColorMajor))
		} else {
			iv.setImageDrawable(d.mutate())
		}
		val c = Cmd(cmd)
		c.view = iv
		iv.setOnClickListener {
			c.onClick(c)
		}
		cmdList.add(c)
		return c
	}

	fun actionText(text: String, cmd: String = text): Cmd {
		val tv = createTextItemView()
		tv.text = text
		val c = Cmd(cmd)
		c.view = tv
		tv.setOnClickListener {
			c.onClick(c)
		}
		cmdList.add(c)
		return c
	}

	private fun popMenu(cmd: Cmd) {
		val p = PopupWindow(context)
		p.width = ViewGroup.LayoutParams.WRAP_CONTENT
		p.height = ViewGroup.LayoutParams.WRAP_CONTENT
		p.isFocusable = true
		p.isOutsideTouchable = true
		p.setBackgroundDrawable(ColorDrawable(0))

		val gd = Shapes.rect {
			cornerListDp(2, 2, 0, 0)
			this.fillColor = Colors.WHITE
			strokeWidthPx = 1
			strokeColor = Colors.LineGray
		}

		val popRootView = context.createLinearVertical()
		popRootView.minimumWidth = dp(150)
		popRootView.backDrawable(gd).padding(5)
		popRootView.divider()
		val itemList = ArrayList<ActionMenuItemInfo>(cmd.items.filter { !it.hidden })
		for (c in itemList) {
			val v = menuItemView(c)
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

		p.showDropUp(cmd.view, 0, -1)
	}

	fun menu(block: Cmd.() -> Unit) {
		val m = find(MENU) ?: actionImage(Res.menu, MENU)
		m.block()
	}

	fun menu(resId: Int, block: Cmd.() -> Unit) {
		val m = find(MENU) ?: actionImage(resId, MENU)
		m.block()
	}

	private fun menuItemView(item: ActionMenuItemInfo): View {
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
		d = d.sized(ImgSize)
		tv.compoundDrawablePadding = dp(10)
		tv.setCompoundDrawables(d, null, null, null)
		tv.textColorMajor()
		return tv
	}

	fun createImageItemView(): ImageView {
		val iv = ImageView(context)
		iv.scaleCenterInside()
		iv.backColorTransFade()
		iv.padding(PAD_HOR, PAD_VER, PAD_HOR, PAD_VER)
		return iv
	}

	fun createTextItemView(): TextView {
		val iv = createTextViewB()
		iv.backColorTransFade()
		iv.textColorMajor()
		iv.gravityCenter()
		iv.minimumWidth = dp(HEIGHT)
		iv.padding(5, 0, 5, 0)
		return iv
	}


	companion object {
		const val MENU = "menu"
		const val ImgSize = 24
		const val HEIGHT = 50// dp
		const val PAD_VER = (HEIGHT - ImgSize) / 2
		const val PAD_HOR = (HEIGHT - ImgSize) / 2
	}
}
