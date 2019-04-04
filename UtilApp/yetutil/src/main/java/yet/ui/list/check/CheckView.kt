package yet.ui.list.check

import android.content.Context
import android.view.View
import android.widget.Checkable
import android.widget.CheckedTextView
import yet.log.fatalIf
import yet.ui.ext.GravityRightCenter
import yet.ui.ext.HeightWrap
import yet.ui.ext.LParam
import yet.ui.ext.WidthFlex
import yet.ui.ext.Wrap
import yet.ui.ext.backColorWhiteFade
import yet.ui.ext.genId
import yet.ui.ext.gravityCenterVertical
import yet.ui.ext.horizontal
import yet.ui.ext.margins
import yet.ui.ext.padding
import yet.ui.ext.rightImage
import yet.ui.list.views.HorItemView
import yet.ui.res.D
import yet.ui.res.sized


class CheckView(context: Context) : HorItemView(context), Checkable {
	val checkView: CheckedTextView
	lateinit var view: View

	init {
		genId()
		horizontal()
		gravityCenterVertical()
		backColorWhiteFade()
		padding(0)

		checkView = CheckedTextView(context).genId()
//		checkView.checkMarkDrawable = D.CheckBox.mutate().sized(16)
		checkView.rightImage(D.CheckBox.mutate().sized(20), 0)
		addView(checkView, LParam.Wrap.GravityRightCenter.margins(10, 0, 10, 0))

	}

	fun bind(view: View): CheckView {
		fatalIf(this.childCount >= 2, "已经绑定了view")
		this.view = view
		addView(view, 0, LParam.WidthFlex.HeightWrap)
		return this
	}

	override fun isChecked(): Boolean {
		return checkView.isChecked
	}

	override fun setChecked(checked: Boolean) {
		if (checked == isChecked) {
			return
		}
		checkView.isChecked = checked
	}

	override fun toggle() {
		isChecked = !isChecked
	}

}