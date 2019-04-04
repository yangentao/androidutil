package yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import yet.theme.Colors
import yet.ui.ext.CenterInParent
import yet.ui.ext.CenterVertical
import yet.ui.ext.HeightFill
import yet.ui.ext.LParam
import yet.ui.ext.ParentLeft
import yet.ui.ext.ParentRight
import yet.ui.ext.RParam
import yet.ui.ext.WidthFill
import yet.ui.ext.WidthWrap
import yet.ui.ext.backColor
import yet.ui.ext.backColorTransFade
import yet.ui.ext.backDrawable
import yet.ui.ext.divider
import yet.ui.ext.dp
import yet.ui.ext.gravityCenter
import yet.ui.ext.gravityLeftCenter
import yet.ui.ext.height
import yet.ui.ext.heightFill
import yet.ui.ext.margins
import yet.ui.ext.onClick
import yet.ui.ext.padding
import yet.ui.ext.scaleCenterCrop
import yet.ui.ext.singleLine
import yet.ui.ext.size
import yet.ui.ext.textColorWhite
import yet.ui.ext.toRightOf
import yet.ui.ext.width
import yet.ui.page.Cmd
import yet.ui.res.D
import yet.ui.res.Res
import yet.ui.res.Shapes
import yet.ui.res.sized
import yet.ui.res.tintedWhite
import yet.ui.viewcreator.createImageView
import yet.ui.viewcreator.createLinearHorizontal
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createTextViewA
import yet.ui.viewcreator.createTextViewB
import yet.util.Task


class TitleBar(context: Context) : RelativeLayout(context) {
	val leftCmds = ArrayList<Cmd>()
	val rightCmds = ArrayList<Cmd>()
	var titleView: View? = null
	var titleCenter = TitleBar.TitleCenter

	var leftLinear: LinearLayout? = null
	var rightLinear: LinearLayout? = null

	var pushModel: Boolean = false
		private set
	private var leftBack = ArrayList<Cmd>()
	private var rightBack = ArrayList<Cmd>()
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

	private fun <T> moveTo(from: ArrayList<T>, dest: ArrayList<T>) {
		dest.clear()
		dest.addAll(from)
		from.clear()
	}

	fun findMenuItem(cmd: String): ActionMenuItemInfo? {
		for (c in leftCmds) {
			val b = c.items.find {
				it.cmd == cmd
			}
			if (b != null) {
				return b
			}
		}
		for (c in rightCmds) {
			val b = c.items.find {
				it.cmd == cmd
			}
			if (b != null) {
				return b
			}
		}
		return null
	}

	fun find(block: (Cmd) -> Boolean): Cmd? {
		return leftCmds.find(block) ?: rightCmds.find(block)
	}

	fun find(cmd: String): Cmd? {
		return find {
			it.cmd == cmd
		}
	}

	fun commit() {
		leftLinear?.removeAllViews()
		rightLinear?.removeAllViews()
		removeAllViews()

		if (leftCmds.isNotEmpty()) {
			val ll = this.createLinearHorizontal()
			for (c in leftCmds) {
				ll.addView(c.view, c.param)
				if (c.items.isEmpty()) {
					c.view.setOnClickListener {
						c.onClick(c)
					}
				} else {
					c.view.setOnClickListener {
						popMenu(c)
					}
				}
			}
			addView(ll, RParam.ParentLeft.HeightFill.WidthWrap)
			leftLinear = ll
		}

		if (rightCmds.isNotEmpty()) {
			val ll = this.createLinearHorizontal()
			for (c in rightCmds) {
				ll.addView(c.view, c.param)
				if (c.items.isEmpty()) {
					c.view.setOnClickListener {
						c.onClick(c)
					}
				} else {
					c.view.setOnClickListener {
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
		leftCmds.removeAll { it.cmd == BACK }
	}

	fun removeCmd(cmd: String) {
		leftCmds.removeAll { it.cmd == cmd }
		rightCmds.removeAll { it.cmd == cmd }
	}


	fun showBack(resId: Int = Res.back): Cmd {
		leftCmds.removeAll { it.cmd == BACK }
		val iv = createImageItemView()
		val d = D.tinted(resId, Colors.WHITE)
		iv.setImageDrawable(d)
		val c = Cmd(BACK)
		c.view = iv
		c.param = LParam.size(HEIGHT)
		leftCmds.add(c)
		return c
	}

	fun actionImage(resId: Int, cmd: String = "$resId"): Cmd {
		return actionImage(Res.drawable(resId), cmd)
	}

	fun actionImage(d: Drawable, cmd: String = Cmd.genCmd): Cmd {
		val iv = createImageItemView()
		iv.setImageDrawable(d.tintedWhite)
		val c = Cmd(cmd)
		c.view = iv
		c.param = LParam.heightFill().width(HEIGHT)
		rightCmds.add(c)
		return c
	}

	fun actionText(text: String, cmd: String = text): Cmd {
		val tv = createTextItemView()
		tv.text = text
		val c = Cmd(cmd)
		c.view = tv
		c.param = LParam.HeightFill.WidthWrap
		rightCmds.add(c)
		return c
	}

	fun leftImage(resId: Int, cmd: String = "$resId"): Cmd {
		return leftImage(Res.drawable(resId), cmd)
	}

	fun leftImage(d: Drawable, cmd: String = Cmd.genCmd): Cmd {
		val iv = createImageItemView()
		iv.setImageDrawable(d.tintedWhite)
		val c = Cmd(cmd)
		c.view = iv
		c.param = LParam.heightFill().width(HEIGHT)
		leftCmds.add(c)
		return c
	}

	fun leftText(text: String, cmd: String = text): Cmd {
		val tv = createTextItemView()
		tv.text = text
		val c = Cmd(cmd)
		c.view = tv
		c.param = LParam.HeightFill.WidthWrap
		leftCmds.add(c)
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
			fillColor = Colors.Theme
			cornerListDp(0, 0, 2, 2)
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
		p.showAsDropDown(cmd.view, 0, 1)
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
		if (item.tintTheme) {
			d = d.tintedWhite.sized(TitleBar.ImgSize)
		} else {
			d = d.sized(TitleBar.ImgSize)
		}
		tv.compoundDrawablePadding = dp(10)
		tv.setCompoundDrawables(d, null, null, null)
		tv.textColorWhite()
		return tv
	}

	fun createImageItemView(): ImageView {
		val iv = ImageView(context)
		iv.scaleCenterCrop()
		iv.backColorTransFade()
		iv.padding(PAD_HOR, PAD_VER, PAD_HOR, PAD_VER)
		return iv
	}

	fun createTextItemView(): TextView {
		val iv = createTextViewB()
		iv.backColorTransFade()
		iv.textColorWhite()
		iv.gravityCenter()
		iv.minimumWidth = dp(HEIGHT)
		iv.padding(5, 0, 5, 0)
		return iv
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