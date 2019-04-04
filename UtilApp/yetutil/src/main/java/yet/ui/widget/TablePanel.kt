package yet.ui.widget

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import yet.theme.Colors
import yet.ui.ext.*
import yet.ui.viewcreator.linearHor

/**
 * Created by entaoyang@163.com on 16/3/13.
 */
/**
 * 分割线的颜色就是TablePanel的背景色, 可以使用padding属性来设置边框
 * 跟TableLayout类似, TableLayout对固定高度的计算和LinearLayout有区别
 */
open class TablePanel(context: Context) : LinearLayout(context) {
	private var rowHeight = 50
	private var horSpace = 1
	private var verSpace = 1

	init {
		vertical()
		this.setBackgroundColor(Colors.PageGray)
		linearParam().widthFill().heightWrap().set(this)
	}

	val itemViewCount: Int
		get() {
			var count = 0
			for (i in 0..rowCount - 1) {
				count += getRow(i).childCount
			}
			return count
		}

	val rowCount: Int
		get() = super.getChildCount()

	fun getRow(index: Int): LinearLayout {
		return super.getChildAt(index) as LinearLayout
	}

	/**
	 * 默认的weight是1

	 * @return
	 */
	fun addRow(heightDp: Int = rowHeight): LinearLayout {
		return linearHor(lParam().widthFill().heightDp(heightDp).margins(0, if (rowCount == 0) 0 else verSpace, 0, 0)) {
			backColorPage()
		}
	}

	fun lastRow(): LinearLayout? {
		if (rowCount > 0) {
			return getRow(rowCount - 1)
		}
		return null
	}

	fun addItemView(view: View, weight: Float = 1f) {
		var row = lastRow() ?: addRow()
		row.addViewParam(view) {
			widthDp(0).weight(weight).heightFill().gravityCenter().margins(if (row.childCount == 0) 0 else horSpace, 0, 0, 0)
		}

	}

	fun clean() {
		removeAllViews()
	}

	fun setSpace(spaceDp: Int) {
		setVerSpace(spaceDp)
		setHorSpace(spaceDp)
	}

	fun setVerSpace(verSpaceDp: Int) {
		this.verSpace = verSpaceDp
	}

	fun setHorSpace(horSpaceDp: Int) {
		this.horSpace = horSpaceDp
	}

	fun setRowHeight(rowHeightDp: Int) {
		this.rowHeight = rowHeightDp
	}

}
