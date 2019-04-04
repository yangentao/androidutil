package yet.ui.ext

import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow

/**
 * Created by entaoyang@163.com on 2018-04-18.
 */

fun PopupWindow.showDropUp(anchor: View, xoff: Int, yoff: Int) {
	val v = this.contentView
	v.measure(makeSpec(this.width), makeSpec(this.height))
	val h = v.measuredHeight + anchor.height
	this.showAsDropDown(anchor, xoff, -h + yoff)
}

private fun makeSpec(measureSpec: Int): Int {
	val mode: Int
	if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
		mode = View.MeasureSpec.UNSPECIFIED
	} else {
		mode = View.MeasureSpec.EXACTLY
	}
	return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode)
}