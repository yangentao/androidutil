package yet.ui.widget

import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import yet.theme.Colors
import yet.theme.IconSize
import yet.ui.res.D
import yet.ui.res.ImageStated
import yet.util.Task
import java.util.*

/**
 * 每个action必须有label或icon, 或两者都有, 其他是可选项.
 * 对已经添加近tabbar,bottombar, titlebar的action item, 可以调用update方法来更新显示.
 * 在ActionPage中不支持children
 *
 *
 * id, tag, label, icon都被支持
 *
 *
 * BottomBar:  risk
 *
 *
 * TitleBar: chilcren将作为子菜单
 *
 *
 * TabBar:selected, num
 *
 *
 * ActionPage: sublabel, sublabelcolor, subicon, subiconleft, margintop
 *
 *
 * Created by yet on 2015/10/12.
 */
class Action(val tag: String) {

	var group: String = ""
	/**
	 * 作为菜单时有效
	 */
	val children: MutableList<Action> = ArrayList()

	val childCount: Int
		get() = children.size
	/**
	 * 菜单时有效， titlbar最右边折叠的时候， 目前不支持
	 */
	var checkable = false
	/**
	 * 菜单时有效， titlbar最右边折叠的时候, 目前不支持
	 */
	var checked = false
	/**
	 * 选中状态, tabbar有效
	 */
	var selected = false
	/**
	 * visible是false的action不被显示
	 */
	var hidden = false
	/**
	 * 标题文本
	 */
	var label: String = ""
	/**
	 * 图标, 大小会根据情况固定, 比如30dp
	 */
	var icon: Drawable? = null

	/**
	 * tabbar每个item右上角显示的小红点, 0:不显示;  <0 :只显示红点;  >0 显示红点和数字
	 */
	var num = 0//红点+数字， 指示作用， 比如有新短信5条， 显示个5,  0会清除红点
	/**
	 * 强调, 会使用C.risk颜色来作为背景色, 比如红色或橙色, 表示危险动作,如删除
	 */
	var risk = false
	var safe = false

	/**
	 * 子标签, 用于ActionPage, 右边显示
	 */
	var subLabel: String? = null
	/**
	 * 颜色设置
	 */
	var subLabelColor = Colors.TextColorMinor
	/**
	 * 右边的图标, 用于AcionPage
	 */
	var subIcon: Drawable? = null
	/**
	 * 第二个textview的左图标
	 */
	var subIconLeft: Drawable? = null
	var subIconLeftSize: Int = 0
	var marginTop = 1

	//用switch展现, checked属性来表示ON/OFF
	var isSwitch = false

	//用户自定义数据
	var argB: Boolean = false
	var argN: Int = 0
	var argL: Long = 0L
	var argS: String = ""
	var argObj: Any? = null

	var clickListener: View.OnClickListener? = null
		private set

	var onUpdate: (Action) -> Unit = {
		_ ->
	}

	var onAction: (Action) -> Unit = {

	}
	var onCheckChanged: (Action) -> Unit = { _ ->

	}

	init {
		if (this.label.isEmpty()) {
			this.label = this.tag
		}
	}

	fun argS(s: String): Action {
		this.argS = s
		return this
	}

	fun argN(n: Int): Action {
		this.argN = n
		return this
	}

	fun argL(n: Long): Action {
		this.argL = n
		return this
	}


	fun argB(b: Boolean): Action {
		this.argB = b
		return this
	}

	fun argObj(obj: Any): Action {
		this.argObj = obj
		return this
	}

	fun switchCheck(on: Boolean): Action {
		isSwitch = true
		checked = on
		return this
	}

	fun onAction(block: (Action) -> Unit): Action {
		this.onAction = block
		return this
	}

	fun setOnClickListener(listener: View.OnClickListener): Action {
		this.clickListener = listener
		return this
	}

	fun isTag(tag: String): Boolean {
		return this.tag == tag
	}

	fun isAction(a: Action): Boolean {
		return this.tag == a.tag
	}


	fun isLabel(label: String): Boolean {
		return this.label  ==  label
	}

	fun commit() {
		Task.fore {
			onUpdate(this@Action)
		}
	}
	fun isGroup(group: String): Boolean {
		return this.group == group
	}
	fun group(group: String): Action {
		this.group = group
		return this
	}

	fun selected(selected: Boolean): Action {
		this.selected = selected
		return this
	}

	fun hidden(hidden: Boolean): Action {
		this.hidden = hidden
		return this
	}


	fun icon(icon: Drawable): Action {
		this.icon = icon
		return this
	}

	fun icon(@DrawableRes resId: Int): Action {
		this.icon = D.res(resId)
		return this
	}
	fun icon(@DrawableRes normalId:Int, @DrawableRes lightId:Int):Action {
		this.icon = ImageStated(normalId).pressed(lightId, true).selected(lightId, true).value
		return this
	}


	fun risk(risk: Boolean): Action {
		this.risk = risk
		return this
	}

	fun safe(safe: Boolean): Action {
		this.safe = safe
		return this
	}


	fun num(num: Int): Action {
		this.num = num
		return this
	}

	fun label(newLabel: String): Action {
		this.label = newLabel
		return this
	}

	fun subLabel(newSubLabel: String?): Action {
		this.subLabel = newSubLabel
		return this
	}

	fun subLabelColor(color: Int): Action {
		subLabelColor = color
		return this
	}

	fun subIcon(@DrawableRes res: Int): Action {
		return subIcon(D.res(res))
	}

	fun subIcon(subIcon: Drawable?): Action {
		this.subIcon = subIcon
		return this
	}

	fun more(arrow: Boolean = true): Action {
		this.subIcon = if (arrow) D.res(net.yet.R.drawable.yet_arrow_right) else null
		return this
	}

	fun subIconLeft(@DrawableRes res: Int, size: Int = IconSize.Small): Action {
		subIconLeft(D.res(res))
		this.subIconLeftSize = size
		return this
	}

	fun subIconLeft(subIconLeft: Drawable, size: Int = IconSize.Small): Action {
		this.subIconLeft = subIconLeft
		this.subIconLeftSize = size
		return this
	}

	fun marginTop(marginTop: Int): Action {
		this.marginTop = marginTop
		return this
	}


	fun add(vararg ac: Action): Action {
		for (a in ac) {
			children.add(a)
		}
		return this
	}

	fun addAction(labelTag: String): Action {
		val a = Action(labelTag)
		children.add(a)
		return a
	}

	fun removeChildByTag(tag: String) {
		for (a in children) {
			if (a.isTag(tag)) {
				children.remove(a)
				return
			}
		}
	}

	override fun hashCode(): Int {
		return tag.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		if (other is Action) {
			return tag == other.tag
		}
		return false
	}

	companion object {
		val MENU = "菜单"
	}
}
