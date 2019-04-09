package yet.ui.ext

import android.graphics.drawable.Drawable
import android.widget.RadioButton
import android.widget.RadioGroup
import dev.entao.appbase.ex.ImageStated
import net.yet.R
import yet.ui.res.D
import yet.ui.res.sized
import yet.ui.viewcreator.createRadioButton

/**
 * Created by entaoyang@163.com on 2016-11-07.
 */

// IMAGE--Title------CHECK
fun <T : RadioButton> T.styleImageTextCheckRes(leftRes: Int): T {
	return this.styleImageTextCheckRes(leftRes, R.drawable.yet_checkbox, R.drawable.yet_checkbox_checked)
}

fun <T : RadioButton> T.styleImageTextCheckRes(leftRes: Int, rightNormal: Int, rightChecked: Int): T {
	val rightImg = ImageStated(D.res(rightNormal)).checked(D.res(rightChecked)).value.sized(15)
	this.buttonDrawable = null
	this.setCompoundDrawables(D.res(leftRes).sized(27), null, rightImg.sized(25), null)
	this.compoundDrawablePadding = dp(15)
	return this
}

fun <T : RadioButton> T.styleImageTextCheck(leftDraw: Drawable?): T {
	return styleImageTextCheck(leftDraw, D.res(R.drawable.yet_checkbox), D.res(R.drawable.yet_checkbox_checked))
}

fun <T : RadioButton> T.styleImageTextCheck(leftDraw: Drawable?, rightNormal: Drawable, rightChecked: Drawable): T {
	val rightImg = ImageStated(rightNormal).checked(rightChecked).value.sized(15)
	this.buttonDrawable = null
	this.setCompoundDrawables(leftDraw?.sized(27), null, rightImg.sized(25), null)
	this.compoundDrawablePadding = dp(15)
	return this
}


fun RadioGroup.addRadioButton(title: String, block: RadioGroup.LayoutParams.() -> RadioGroup.LayoutParams): RadioButton {
	val view = this.context.createRadioButton()
	view.text = title
	val lp = RadioParam.HeightButton.WidthFill
	lp.block()
	this.addView(view, lp)
	return view
}


val RadioParam: RadioGroup.LayoutParams
	get() {
		return RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT)
	}
