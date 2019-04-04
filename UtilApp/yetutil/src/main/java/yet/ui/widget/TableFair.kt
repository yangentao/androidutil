package yet.ui.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout
import android.widget.TableLayout
import android.widget.TableRow
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.viewcreator.createRelative
import java.util.*

/**
 * 等分空间的table, 需要指定列数, 跟gridview类似
 * 分割线的颜色就是TablePanel的背景色, 可以使用padding属性来设置边框
 * 例子
 */
open class TableFair(context: Context) : TableLayout(context) {
	var columns = 2

	var horSpace = 1
	var verSpace = 1

	var lineColor = Colors.LineGray
	var horDivider: Divider? = null
	var verDivider: Divider? = null

	private var colorNormal = Color.WHITE
	private var colorPressed = Colors.Fade

	var onItemClick: (Int) -> Unit = { _ -> }

	var cellLayoutParam: RelativeLayout.LayoutParams = relativeParam().wrap().centerInParent()

	private val onClickListener = View.OnClickListener { v ->
		val pos = v.tag as Int
		if (pos >= 0) {
			onItemClick(pos)
		}
	}


	init {
		this.setBackgroundColor(Color.LTGRAY)
	}

	fun clean() {
		removeAllViews()
	}

	fun setItemSelector(colorNormal: Int, colorPressed: Int) {
		this.colorNormal = colorNormal
		this.colorPressed = colorPressed
	}

	fun setItemsViewsBlock(block: (ArrayList<View>) -> Unit) {
		val ls = ArrayList<View>()
		block(ls)
		setItemViews(ls)
	}


	fun setItemViews(viewList: List<View>) {
		clean()

		if (horDivider != null) {
			this.divider(horDivider!!)
		} else {
			if (horSpace > 0) {
				this.divider(Divider(lineColor).mid().size(horSpace))
			}
		}
		var i = 0
		while (i < viewList.size) {
			val row = TableRow(context).genId()
			addView(row, TParam.widthFill().height(0).weight(1f))
			if (verDivider != null) {
				row.divider(verDivider!!)
			} else {
				if (verSpace > 0) {
					row.divider(Divider().mid().size(verSpace))
				}
			}
			for (c in 0..columns - 1) {
				val n = i + c
				val rl = context.createRelative()
				rl.backColor(colorNormal, colorPressed)
				rl.tag = n
				rl.setOnClickListener(this.onClickListener)
				row.addView(rl, RowParam.width(0).weight(1f).heightFill().gravityCenter())
				if (n < viewList.size) {
					val view = viewList[n]
					rl.addView(view, cellLayoutParam)
				}
			}
			i += columns
		}
	}


	fun setSpace(spaceDp: Int) {
		verSpace = spaceDp
		horSpace = spaceDp
	}
}
