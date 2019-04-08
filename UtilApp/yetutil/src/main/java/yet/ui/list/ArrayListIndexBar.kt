package yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import dev.entao.base.BlockUnit
import yet.ext.RGB
import yet.ui.ext.*
import yet.ui.res.Shapes
import yet.ui.util.LayerUtil
import yet.ui.viewcreator.textView
import yet.util.Task
import java.util.*

/**
 * Created by yet on 2015/10/29.
 */
class ArrayListIndexBar(context: Context, feedbackParentView: RelativeLayout) : LinearLayout(context) {

	private var selectView: View? = null
	private val selectDrawable = bgDraw()
	private val darkColor = RGB("#ccc")
	private val normalColor = Color.TRANSPARENT
	private var feedbackView: TextView

	private var tagList: ArrayList<Char>? = null
	private var tagPosMap = HashMap<Char, Int>(30)

	private val hideFeedbackRun: BlockUnit

	var onIndexChanged: (Int) -> Unit = {}
	var onIndexBarVisiblityChanged: (Int) -> Unit = { }

	private val touchListener = View.OnTouchListener { _, event ->
		val action = event.actionMasked
		val y = event.y.toInt()
		if (action == MotionEvent.ACTION_DOWN) {
			setBackgroundColor(darkColor)
		} else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_OUTSIDE) {
			setBackgroundColor(normalColor)
			selectByY(y)
		} else if (action == MotionEvent.ACTION_MOVE) {
			selectByY(y)
		}
		false
	}

	init {
		orientationVertical().gravityCenterHorizontal().padding(0, 0, 0, 0).makeClickable()
		feedbackView = feedbackParentView.textView(relativeParam().centerInParent().size(70)) {
			textColor_(Color.WHITE).textSize_(50).gravityCenter().backDrawable(
					Shapes.rect {
						cornerPx = dp(10)
						fillColor = RGB("#555")
						strokeWidthPx = dp(2)
						strokeColor = RGB("#ddd")
					}

			).gone()
		}
		hideFeedbackRun =  { feedbackView.gone() }
		this.setOnTouchListener(touchListener)
	}


	private fun selectByY(y: Int) {
		for (i in 0..childCount - 1) {
			val itemView = getChildAt(i)
			if (y >= itemView.top && y <= itemView.bottom) {
				if (selectView !== itemView) {
					val tv = itemView as TextView
					val tag = tv.text.toString()[0]
					select(tag, true)
					Task.fore {
						onIndexChanged(tagPosMap[tag]!!)
					}
				}
				break
			}
		}
	}

	fun select(tag: Char) {
		Task.fore {
			select(tag, false)
		}
	}

	private fun select(tag: Char, feedback: Boolean) {
		val tagIndex = tagList!!.indexOf(tag)
		if (tagIndex >= 0) {
			if (selectView != null) {
				selectView!!.setBackgroundColor(Color.TRANSPARENT)
			}
			selectView = getChildAt(tagIndex)
			selectView!!.background = selectDrawable

			val str = (selectView as TextView).text.toString()
			feedbackView.text(str)
			if (feedback) {
				feedbackView.visiable()
				Task.foreDelay(650, hideFeedbackRun)
			}
		}
	}

	fun buildViews(tagList: ArrayList<Char>, tagPosMap: HashMap<Char, Int>) {
		this.tagList = tagList
		this.tagPosMap = tagPosMap
		removeAllViews()
		for (s in tagList) {
			textView(lParam().width(40).height_(0).weight(1).gravityCenter()) {
				this.tag = s
				text_(s.toString()).textSizeD().textColor(Color.BLACK).gravityCenter()
			}
		}
	}


	private fun bgDraw(): Drawable {
		val gd = GradientDrawable()
		gd.setColor(Color.GRAY)
		gd.setStroke(2, Color.WHITE)
		gd.setCornerRadius(5f)

		val lu = LayerUtil()
		lu.add(gd, 6, 0, 6, 0)
		lu.add(ColorDrawable(Color.TRANSPARENT))
		return lu.get()
	}

	override fun setVisibility(visibility: Int) {
		super.setVisibility(visibility)
		onIndexBarVisiblityChanged(visibility)
	}

	fun postHide() {
		Task.fore{ visibility = View.GONE }
	}

	fun postShow() {
		Task.fore{ visibility = View.VISIBLE }
	}
}
