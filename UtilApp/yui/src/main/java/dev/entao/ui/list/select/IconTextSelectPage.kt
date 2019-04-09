package dev.entao.ui.list.select

import android.content.Context
import android.view.View
import dev.entao.theme.IconSize
import dev.entao.theme.TextSize
import dev.entao.ui.list.views.IconTextView

/**
 * Created by entaoyang@163.com on 2017-01-05.
 */

abstract class IconTextSelectPage : SelectPage() {


	override fun onNewView(context: Context, position: Int): View {
		val v = IconTextView(context, IconSize.Big)
		v.setTextSize(TextSize.Normal)

		return v
	}
}
