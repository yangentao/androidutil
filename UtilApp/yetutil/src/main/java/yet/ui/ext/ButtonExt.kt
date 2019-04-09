package yet.ui.ext

import android.widget.Button
import android.widget.CheckBox
import dev.entao.appbase.ex.Colors
import yet.theme.ViewSize
import yet.ui.res.D

/**
 * Created by entaoyang@163.com on 16/4/28.
 */

fun <T : Button> T.styleGreen(corner: Int = 2): T {
	this.textSizeB().textColor(Colors.WHITE).backDrawable(D.buttonGreen(corner))
	return this
}

fun <T : Button> T.styleGreenRound(): T {
	this.textSizeB().textColor(Colors.WHITE).backDrawable(D.buttonGreen(ViewSize.ButtonHeight / 2))
	return this
}

fun <T : Button> T.styleGrayRound(): T {
	this.textSizeB().textColor(Colors.TextColorMajor).backDrawable(D.buttonGreen(ViewSize.ButtonHeight / 2))
	return this
}

fun <T : Button> T.styleRed(corner: Int = 2): T {
	this.textSizeB().textColor(Colors.WHITE).backDrawable(D.buttonRed(corner))
	return this
}

fun <T : Button> T.styleRedRound(): T {
	this.textSizeB().textColor(Colors.WHITE).backDrawable(D.buttonRed(ViewSize.ButtonHeight / 2))
	return this
}

fun <T : Button> T.styleWhite(corner: Int = 2): T {
	this.textSizeB().textColorMajor().backDrawable(D.buttonWhite(corner))
	return this
}

fun <T : Button> T.styleWhiteRound(): T {
	this.textSizeB().textColorMajor().backDrawable(D.buttonWhite(ViewSize.ButtonHeight / 2))
	return this
}

fun <T : CheckBox> T.styleSwitch(): T {
//	val w = 50
//	val h = 30
//	val dd1 = makeRoundEdgeRectDrawable(w, h, C.WHITE, 1, C.lightGrayColor)
//	val dd2 = makeRoundEdgeRectDrawable(w, h, C.safeColor)
////		val dd3 = makeRoundEdgeRectDrawable(w, h, C.lightGrayColor, 1, C.WHITE)
////		cb.checkMarkDrawable = StateImage(dd1).checked(dd2).enabled(dd3, false).value
////		cb.checkMarkDrawable = StateImage(dd1).checked(dd2).value
//	val draw = StateImage(dd1).checked(dd2).value
////		cb.setBackgroundDrawable(StateImage(dd1).checked(dd2).value)
////		cb.checkMarkDrawable = Res.drawable("checkbox", true)
//	cb.buttonDrawable = draw
//	cb.backColorPage()
//	cb.compoundDrawablePadding = 0
//	this.textSizeB().textColorMajor().backDrawable(ShapeDef.whiteButton())
	return this
}
