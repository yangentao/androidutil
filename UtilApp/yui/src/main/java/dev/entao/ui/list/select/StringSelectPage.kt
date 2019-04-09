package dev.entao.ui.list.select

import android.view.View
import dev.entao.ui.list.views.TextItemView

/**
 * 简单字符串选择
 */
open class StringSelectPage : TextSelectPage() {

	override fun onBindView(itemView: View, position: Int) {
		(itemView as TextItemView).text = getItem(position) as String
	}
}
