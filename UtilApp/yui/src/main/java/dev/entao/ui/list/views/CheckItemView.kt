package dev.entao.ui.list.views

import android.content.Context
import android.view.View
import android.widget.CheckedTextView
import dev.entao.ui.ext.*
import dev.entao.ui.res.D

/**
 * Created by entaoyang@163.com on 16/3/13.
 */


open class CheckItemView(context: Context, val itemView: View, listener: CheckStatusListener? = null) : HorItemView(context), ItemViewCheckable {
	protected var checkView: CheckedTextView
		private set
	private var listener: CheckStatusListener? = null

	private val MR = 10

	init {
		padding(0, 0, 0, 0)
		this.listener = listener
		addViewParam(itemView, 0) { widthDp(0).weight(1f).heightWrap() }

		checkView = CheckedTextView(context).genId().gone()
		checkView.checkMarkDrawable = D.CheckBox
		addViewParam(checkView) { wrap().gravityRightCenter().margins(10, 0, MR, 0) }

		if (this.listener == null && itemView is CheckStatusListener) {
			this.listener = itemView
		}
	}


	override fun isChecked(): Boolean {
		return checkView.isChecked
	}

	override fun setChecked(checked: Boolean) {
		if (checked == isChecked) {
			return
		}
		checkView.isChecked = checked
		listener?.onItemCheckChanged(this, checked)
	}

	override fun toggle() {
		isChecked = !isChecked
	}

	override var isCheckModel: Boolean
		get() = checkView.isVisiable()
		set(checkModel) {
			if (isCheckModel == checkModel) {
				return
			}
			if (checkModel) {
				isChecked = false
				checkView.visiable()
			} else {
				checkView.gone()
			}
			listener?.onItemCheckModelChanged(this, checkModel)
		}

	override fun setCheckboxMarginRight(dp: Int) {
		linearParam().wrap().gravityRightCenter().margins(10, 0, MR + dp, 0).set(checkView)
		this.requestLayout()
	}

	interface CheckStatusListener {
		fun onItemCheckModelChanged(checkItemView: CheckItemView, checkModel: Boolean)

		fun onItemCheckChanged(checkItemView: CheckItemView, check: Boolean)
	}
}