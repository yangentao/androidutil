//package net.yet.ui.ext
//
//import android.support.constraint.ConstraintLayout
//import android.view.View
//import net.yet.util.RefUtil
//
///**
// * Created by entaoyang@163.com on 2016-11-03.
// */
//
//
//fun <T : ConstraintLayout> T.addViewParam(view: View, f: ConstraintLayout.LayoutParams.() -> ConstraintLayout.LayoutParams): T {
//	val lp = constraintParam()
//	lp.f()
//	this.addView(view, lp)
//	return this
//}
//
//fun <T : ConstraintLayout> T.addViewParam(view: View, index: Int, f: ConstraintLayout.LayoutParams.() -> ConstraintLayout.LayoutParams): T {
//	val lp = constraintParam()
//	lp.f()
//	this.addView(view, index, lp)
//	return this
//}
//
//
//fun constraintParam(): ConstraintLayout.LayoutParams {
//	val p = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
//	return p
//}
//
////center
//fun ConstraintLayout.LayoutParams.centerInParent(): ConstraintLayout.LayoutParams {
//	this.leftToLeftOfParent()
//	this.topToTopOfParent()
//	this.rightToLeftOfParent()
//	this.bottomToBottomOfParent()
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.centerHorInParent(): ConstraintLayout.LayoutParams {
//	this.leftToLeftOfParent()
//	this.rightToLeftOfParent()
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.centerVerInParent(): ConstraintLayout.LayoutParams {
//	this.topToTopOfParent()
//	this.bottomToBottomOfParent()
//	return this
//}
//
////left-left
//fun ConstraintLayout.LayoutParams.leftToLeftOfParent(): ConstraintLayout.LayoutParams {
//	this.leftToLeft = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.leftToLeft(view: View): ConstraintLayout.LayoutParams {
//	this.leftToLeft = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.leftToLeft(id: Int): ConstraintLayout.LayoutParams {
//	this.leftToLeft = id
//	return this
//}
//
////left-right
//fun ConstraintLayout.LayoutParams.leftToRightOfParent(): ConstraintLayout.LayoutParams {
//	this.leftToRight = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.leftToRight(view: View): ConstraintLayout.LayoutParams {
//	this.leftToRight = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.leftToRight(id: Int): ConstraintLayout.LayoutParams {
//	this.leftToRight = id
//	return this
//}
//
//
////right-left
//fun ConstraintLayout.LayoutParams.rightToLeftOfParent(): ConstraintLayout.LayoutParams {
//	this.rightToLeft = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.rightToLeft(view: View): ConstraintLayout.LayoutParams {
//	this.rightToLeft = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.rightToLeft(id: Int): ConstraintLayout.LayoutParams {
//	this.rightToLeft = id
//	return this
//}
//
////right-right
//
//fun ConstraintLayout.LayoutParams.righToRightOfParent(): ConstraintLayout.LayoutParams {
//	this.rightToRight = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.righToRight(view: View): ConstraintLayout.LayoutParams {
//	this.rightToRight = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.righToRight(id: Int): ConstraintLayout.LayoutParams {
//	this.rightToRight = id
//	return this
//}
//
////top - top
//fun ConstraintLayout.LayoutParams.topToTopOfParent(): ConstraintLayout.LayoutParams {
//	this.topToTop = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.topToTop(view: View): ConstraintLayout.LayoutParams {
//	this.topToTop = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.topToTop(id: Int): ConstraintLayout.LayoutParams {
//	this.topToTop = id
//	return this
//}
//
////top-bottom
//fun ConstraintLayout.LayoutParams.topToBottomOfParent(): ConstraintLayout.LayoutParams {
//	this.topToBottom = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.topToBottom(view: View): ConstraintLayout.LayoutParams {
//	this.topToBottom = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.topToBottom(id: Int): ConstraintLayout.LayoutParams {
//	this.topToBottom = id
//	return this
//}
//
////bottom-bottom
//fun ConstraintLayout.LayoutParams.bottomToBottomOfParent(): ConstraintLayout.LayoutParams {
//	this.bottomToBottom = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.bottomToBottom(view: View): ConstraintLayout.LayoutParams {
//	this.bottomToBottom = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.bottomToBottom(id: Int): ConstraintLayout.LayoutParams {
//	this.bottomToBottom = id
//	return this
//}
//
////bottom- top
//fun ConstraintLayout.LayoutParams.bottomToTopOfParent(): ConstraintLayout.LayoutParams {
//	this.bottomToTop = 0
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.bottomToTop(view: View): ConstraintLayout.LayoutParams {
//	this.bottomToTop = view.id
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.bottomToTop(id: Int): ConstraintLayout.LayoutParams {
//	this.bottomToTop = id
//	return this
//}
//
//
////width
//fun ConstraintLayout.LayoutParams.fillWidth(): ConstraintLayout.LayoutParams {
//	this.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.width(w: Int): ConstraintLayout.LayoutParams {
//	this.width = dp(w)
//	return this
//}
//
////height
//fun ConstraintLayout.LayoutParams.fillHeight(): ConstraintLayout.LayoutParams {
//	this.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.height(h: Int): ConstraintLayout.LayoutParams {
//	this.height = dp(h)
//	return this
//}
//
////orientation
//fun ConstraintLayout.LayoutParams.vertical(h: Int): ConstraintLayout.LayoutParams {
//	this.orientation = ConstraintLayout.LayoutParams.VERTICAL
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.horizontal(h: Int): ConstraintLayout.LayoutParams {
//	this.orientation = ConstraintLayout.LayoutParams.HORIZONTAL
//	return this
//}
//
////gone margin
//fun ConstraintLayout.LayoutParams.goneLeftMargin(n: Int): ConstraintLayout.LayoutParams {
//	this.goneLeftMargin = n
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.goneRightMargin(n: Int): ConstraintLayout.LayoutParams {
//	this.goneRightMargin = n
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.goneTopMargin(n: Int): ConstraintLayout.LayoutParams {
//	this.goneTopMargin = n
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.goneBottomMargin(n: Int): ConstraintLayout.LayoutParams {
//	this.goneBottomMargin = n
//	return this
//}
//
////bias
//fun ConstraintLayout.LayoutParams.biasHor(f: Double): ConstraintLayout.LayoutParams {
//	this.horizontalBias = f.toFloat()
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.biasVer(f: Double): ConstraintLayout.LayoutParams {
//	this.verticalBias = f.toFloat()
//	return this
//}
//
////ratio
////"3:1",  "H,3:1", "V:3:1", "0.25"
//fun ConstraintLayout.LayoutParams.ratioW(w: Double, h: Double): ConstraintLayout.LayoutParams {
//	this.dimensionRatio = "W,$w:$h"
//	RefUtil.set(this, "dimensionRatioSide", 0)
//	RefUtil.set(this, "dimensionRatioValue", w.toFloat() / h.toFloat())
//	return this
//}
//
////
//fun ConstraintLayout.LayoutParams.ratioH(w: Double, h: Double): ConstraintLayout.LayoutParams {
//	this.dimensionRatio = "H,$w:$h"
//	RefUtil.set(this, "dimensionRatioSide", 1)
//	RefUtil.set(this, "dimensionRatioValue", h.toFloat() / w.toFloat())
//	return this
//}
//
////
//fun ConstraintLayout.LayoutParams.ratio(w: Double, h: Double): ConstraintLayout.LayoutParams {
////	this.dimensionRatio = "$w:$h"
//	RefUtil.setE(this, "dimensionRatioValue", w.toFloat() / h.toFloat())
//	return this
//}
//
//
////weight
//fun ConstraintLayout.LayoutParams.weightHor(w: Double): ConstraintLayout.LayoutParams {
//	this.horizontalWeight = w.toFloat()
//	return this
//}
//
//fun ConstraintLayout.LayoutParams.weightVer(w: Double): ConstraintLayout.LayoutParams {
//	this.verticalWeight = w.toFloat()
//	return this
//}