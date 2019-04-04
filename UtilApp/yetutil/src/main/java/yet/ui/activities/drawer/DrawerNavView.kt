package yet.ui.activities.drawer

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.ScrollView
import yet.theme.Colors
import yet.theme.IconSize
import yet.theme.Space
import yet.ui.ext.*
import yet.ui.res.sized
import yet.ui.viewcreator.createLinearHorizontal
import yet.ui.viewcreator.createLinearVertical
import yet.ui.viewcreator.createTextViewB
import yet.ui.viewcreator.createTextViewC
import yet.ui.widget.Action
import yet.util.Task
import java.util.*

/**
 * Created by entaoyang@163.com on 16/6/27.
 */
class DrawerNavView(context: Context) : LinearLayout(context) {
	var headerLayout: LinearLayout
	var actionLayout: LinearLayout
	var bottomLayout: LinearLayout


	var onActionCallback: (Action) -> Unit = {}

	init {
		vertical().backColor(Colors.Theme).padding(Space.Normal)
		headerLayout = context.createLinearVertical().padding(Space.Small).backDrawable(rectDraw(Colors.Theme)).gravityLeftCenter()
		addViewParam(headerLayout) {
			width(WIDTH).height(HEADER_HEIGHT)
		}

		val lineView = View(context).backColorWhite()
		addViewParam(lineView) {
			width(WIDTH).height(1)
		}

		actionLayout = context.createLinearVertical().padding(0, Space.Small, 0, Space.Small)
		val scrollView = ScrollView(context)
		scrollView.addView(actionLayout, layoutParam().widthFill().heightWrap())
		addViewParam(scrollView) {
			width(WIDTH).height(0).weight(1f)
		}

		bottomLayout = context.createLinearHorizontal().gravityRightCenter()
		addViewParam(bottomLayout) {
			widthFill().heightWrap().gravityRightCenter()
		}
	}

	fun hideBottomBar() {
		bottomLayout.gone()
	}

	fun showBottomBar() {
		bottomLayout.visiable()
	}

	fun setBottomAction(a: Action) {
		val ls = ArrayList<Action>()
		ls.add(a)
		setBottomActions(ls)
	}

	private val bottomActions = ArrayList<Action>()

	fun setBottomActions(ls: List<Action>?) {
		bottomActions.clear()
		if (ls != null) {
			bottomActions.addAll(ls)
		}
		rebuildBottomBar()
	}

	private fun rebuildBottomBar() {
		bottomLayout.removeAllViews()
		for (a in bottomActions) {
			a.onUpdate = {
				postRebuild()
			}
			if (a.hidden) {
				continue
			}
			val textView = context.createTextViewB().textColorWhite().backColor(Color.TRANSPARENT, Colors.Fade).padding(5).text(a.label)
			val d = a.icon
			if (d != null) {
				textView.leftImage(d.sized(IconSize.Normal))
			}
			bottomLayout.addViewParam(textView) {
				widthWrap().heightFill().gravityRightCenter()
			}
			textView.tag = a
			textView.onClick { v ->
				onActionCallback(v.tag as Action)
			}

		}
	}

	var header: View
		get() = headerLayout.getChildAt(0)
		set(view) {
			if (headerLayout.childCount > 0) {
				headerLayout.removeAllViews()
			}
			headerLayout.addViewParam(view) {
				widthWrap().heightWrap().gravityLeftCenter()
			}
		}

	private val actions = ArrayList<Action>()

	fun findAction(tag: String): Action? {
		for (a in actions) {
			if (a.isTag(tag)) {
				return a
			}
		}
		return null
	}

	fun setActions(actions: List<Action>?) {
		this.actions.clear()
		if (actions != null) {
			this.actions.addAll(actions)
		}
		rebuildActions()
	}

	fun postRebuild() {
		Task.fore {
			rebuildActions()
			rebuildBottomBar()
		}
	}

	private fun rebuildActions() {
		var needIcon = false
		for (a in actions) {
			if (a.icon != null) {
				needIcon = true
				break
			}
		}
		actionLayout.removeAllViews()
		for (a in actions) {
			a.onUpdate = {
				postRebuild()
			}
			if (a.hidden) {
				continue
			}
			val tv = context.createTextViewB().textColorWhite().text(a.label)
			if (needIcon) {
				val d: Drawable = a.icon ?: ColorDrawable(Color.TRANSPARENT)
				tv.leftImage(d.sized(IconSize.Normal))
			}
			val ll = context.createLinearHorizontal().backColor(Color.TRANSPARENT, Colors.Fade).padding(5)
			ll.addViewParam(tv) {
				width(0).weight(1f).heightFill().gravityLeftCenter()
			}
			if (a.subLabel != null || a.subIcon != null) {
				val subTv = context.createTextViewC().text(a.subLabel)
				if (a.subIcon != null) {
					subTv.rightImage(a.subIcon!!.sized(IconSize.Small))
				}
				ll.addViewParam(subTv) {
					widthWrap().heightFill().gravityRightCenter()
				}
			}
			if (a.checkable) {
				val sb = SwitchButton(context)
				sb.tag = a
				sb.isChecked = a.checked
				ll.addViewParam(sb) {
					width(SwitchButton.WIDTH).height(SwitchButton.HEIGHT).gravityRightCenter()
				}
				sb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, _ ->
					val ac = buttonView.tag as Action
					ac.checked = buttonView.isChecked
					onActionCallback(ac)
				})
			}

			actionLayout.addViewParam(ll) {
				widthFill().height(40).gravityLeftCenter()
			}
			ll.tag = a
			if (!a.checkable) {
				ll.setOnClickListener { v ->
					onActionCallback(v.tag as Action)
				}
			}
		}
	}


	companion object {
		val HEADER_HEIGHT = 160
		val WIDTH = 256

		private fun rectDraw(color: Int): GradientDrawable {
			val gd = GradientDrawable()
			gd.shape = GradientDrawable.RECTANGLE
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				gd.gradientType = GradientDrawable.LINEAR_GRADIENT
				gd.orientation = GradientDrawable.Orientation.TR_BL
				gd.colors = intArrayOf(addColorN(color, 0x28), addColorN(color, 0x18), color)
			} else {
				gd.setColor(color)
			}
			return gd
		}

		//percent,  1.2,  0.8
		private fun makeColor(c: Int, percent: Float): Int {
			var r = Color.red(c)
			var g = Color.green(c)
			var b = Color.blue(c)
			val a = Color.alpha(c)

			r *= percent.toInt()
			if (r > 255) {
				r = 255
			}
			g *= percent.toInt()
			if (g > 255) {
				g = 255
			}
			b *= percent.toInt()
			if (b > 255) {
				b = 255
			}
			return Color.argb(a, r, g, b)
		}

		private fun addColorN(c: Int, delta: Int): Int {
			var r = Color.red(c)
			var g = Color.green(c)
			var b = Color.blue(c)
			val a = Color.alpha(c)

			r += delta
			if (r > 255) {
				r = 255
			}
			if (r < 0) {
				r = 0
			}
			g += delta
			if (g > 255) {
				g = 255
			}
			if (g < 0) {
				g = 0
			}
			b += delta
			if (b > 255) {
				b = 255
			}
			if (b < 0) {
				b = 0
			}
			return Color.argb(a, r, g, b)
		}
	}
}
