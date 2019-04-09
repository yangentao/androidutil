package dev.entao.ui.activities.drawer

import android.content.Context
import android.widget.LinearLayout
import dev.entao.ui.ext.orientationVertical

/**
 * Created by entaoyang@163.com on 16/6/27.
 */
open class NavActionPanel(context: Context) : LinearLayout(context) {
	init {
		orientationVertical()
	}
}
