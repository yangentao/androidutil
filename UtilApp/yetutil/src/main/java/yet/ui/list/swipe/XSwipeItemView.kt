package yet.ui.list.swipe

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.log.Yog
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.list.views.HorItemView
import yet.ui.list.views.ItemViewCheckable
import yet.ui.res.sized
import yet.ui.viewcreator.createLinearHorizontal
import yet.ui.viewcreator.createTextViewA
import yet.ui.widget.Action
import java.util.*

interface SwipeItemListener {
	fun onSwipeItemAction(swipeItemView: XSwipeItemView, actionView: View, action: Action)
}

open class XSwipeItemView(context: Context, val itemView: View) : HorItemView(context), ItemViewCheckable, View.OnClickListener {


	private val leftViews: LinearLayout
	private val rightViews: LinearLayout
	private val leftActions = ArrayList<Action>()
	private val rightActions = ArrayList<Action>()

	var leftViewWidth = 0
		private set
	var rightViewWidth = 0
		private set

	private var changed = false

	private var stub: ItemViewCheckable? = null
	var onSwipeItemAction: (XSwipeItemView, View, Action) -> Unit = {
		_, _, _ ->
	}

	override fun onClick(v: View) {
		onSwipeItemAction(this@XSwipeItemView, v, v.tag as Action)
	}

	init {
		this.padding(0)
		this.layoutParams = listParam()
		leftViews = context.createLinearHorizontal()
		rightViews = context.createLinearHorizontal()
		changed = true
		if (itemView is ItemViewCheckable) {
			stub = itemView
		}
	}

	fun addLeft(action: Action): XSwipeItemView {
		leftActions.add(action)
		changed = true
		return this
	}

	fun addLeftAction(tagLabel: String): Action {
		val action = Action(tagLabel)
		leftActions.add(action)
		changed = true
		return action
	}

	fun addRight(action: Action): XSwipeItemView {
		rightActions.add(action)
		changed = true
		return this
	}

	fun addRightAction(tagLabel: String): Action {
		val action = Action(tagLabel)
		rightActions.add(action)
		changed = true
		return action
	}

	fun cleanActions(): XSwipeItemView {
		leftActions.clear()
		rightActions.clear()
		changed = true
		return this
	}

	fun commit(): XSwipeItemView {
		if (!changed) {
			Yog.d("NO change found ", javaClass.simpleName)
			return this
		}

		this.removeAllViews()
		leftViews.removeAllViews()
		rightViews.removeAllViews()
		leftViewWidth = 0
		rightViewWidth = 0
		if (leftActions.size > 0) {
			var first = true
			for (action in leftActions) {
				if (!action.hidden) {
					val v = createActionView(action)
					if (first) {
						leftViews.addView(v, linearParam().heightFill().widthWrap())
					} else {
						leftViews.addView(v, linearParam().heightFill().widthWrap().margins(1, 0, 0, 0))
					}
					first = false
				}
			}
			leftViews.measure(0, 0)
			leftViewWidth = leftViews.measuredWidth
		}
		if (rightActions.size > 0) {
			var first = true
			for (action in rightActions) {
				if (!action.hidden) {
					val v = createActionView(action)
					if (first) {
						rightViews.addView(v, linearParam().heightFill().widthWrap())
					} else {
						rightViews.addView(v, linearParam().heightFill().widthWrap().margins(1, 0, 0, 0))
					}
					first = false
				}
			}
			rightViews.measure(0, 0)
			rightViewWidth = rightViews.measuredWidth
		}
		addView(leftViews, linearParam().heightFill().widthWrap())
		addView(onAddItemView(itemView), linearParam().fill())
		addView(rightViews, linearParam().heightFill().widthPx(rightViewWidth))
		setPadding(-leftViewWidth, 0, 0, 0)
		changed = false
		return this
	}

	open fun onAddItemView(itemView: View): View {
		return itemView
	}

	private fun createActionView(action: Action): TextView {
		val context = context
		val tv = context.createTextViewA()
		tv.textX(action.label).miniWidthDp(ACTION_MIN_WIDTH_DP).gravityCenter()
		if (action.risk) {
			tv.textColor(Color.WHITE).backColor(Colors.Risk)
		} else {
			tv.textColor(Color.WHITE).backColor(Colors.Safe)
		}
		if (action.icon != null) {
			val d = action.icon
			tv.setCompoundDrawables(d?.sized(ICON_SIZE_DP), null, null, null)
		}
		action.setOnClickListener(this)
		tv.tag = action
		tv.setOnClickListener(this)
		return tv
	}

	fun findActionView(x: Int): View? {
		var xx = x
		xx += this.scrollX
		for (i in 0..leftViews.childCount - 1) {
			val v = leftViews.getChildAt(i)
			var x0 = v.x.toInt()
			x0 += leftViews.x.toInt()
			if (xx >= x0 && xx <= x0 + v.width) {
				return v
			}
		}
		for (i in 0..rightViews.childCount - 1) {
			val v = rightViews.getChildAt(i)
			var x0 = v.x.toInt()
			x0 += rightViews.x.toInt()
			if (xx >= x0 && xx <= x0 + v.width) {
				return v
			}
		}
		return null
	}

	open fun onActionUpdate(action: Action) {
		commit()
	}

	override var isCheckModel: Boolean
		get() {
			if (stub != null) {
				return stub!!.isCheckModel
			}
			return false
		}
		set(checkModel) = if (stub != null) {
			stub!!.isCheckModel = checkModel
		} else {

		}

	override fun setCheckboxMarginRight(dp: Int) {
		if (stub != null) {
			stub!!.setCheckboxMarginRight(dp)
		}
	}

	override fun setChecked(checked: Boolean) {
		if (stub != null) {
			stub!!.isChecked = checked
		}
	}

	override fun isChecked(): Boolean {
		if (stub != null) {
			return stub!!.isChecked
		}
		return false
	}

	override fun toggle() {
		if (stub != null) {
			stub!!.toggle()
		}
	}

	companion object {

		private val ACTION_MIN_WIDTH_DP = 70
		private val ICON_SIZE_DP = 25
	}
}
