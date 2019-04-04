package yet.ui.grid

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.GridView
import yet.theme.Colors


/**
 * Created by entaoyang@163.com on 2018-03-19.
 */
open class LineGridView(context: Context) : GridView(context) {

	var enableLine = false
	//px
	var lineBottom = true
	var lineTop = false
	var lineRight = false
	var lineLeft = false
	var lineColor = Colors.LineGray
	var lineWidth = 1
	private val localPaint = Paint()


	override fun dispatchDraw(canvas: Canvas) {
		super.dispatchDraw(canvas)
		if (!enableLine) {
			return
		}
		val childCount = this.childCount
		if (childCount <= 0) {
			return
		}
		localPaint.style = Paint.Style.STROKE //画笔实心
		localPaint.color = lineColor//画笔颜色
		localPaint.strokeWidth = lineWidth.toFloat()
		val colCount = this.numColumns
		for (i in 0 until childCount) {
			val cellView = getChildAt(i)
			//画item下边分割线
			canvas.drawLine(cellView.left.toFloat(), cellView.bottom.toFloat(), cellView.right.toFloat(), cellView.bottom.toFloat(), localPaint)
			//画item右边分割线
			if ((i + 1) % colCount != 0) {
				canvas.drawLine(cellView.right.toFloat(), cellView.top.toFloat(), cellView.right.toFloat(), cellView.bottom.toFloat(), localPaint)
			}
		}
	}


}