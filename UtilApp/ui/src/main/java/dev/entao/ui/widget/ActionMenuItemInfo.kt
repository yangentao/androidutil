package dev.entao.ui.widget

import android.graphics.drawable.Drawable

/**
 * Created by entaoyang@163.com on 2018-04-18.
 */

class ActionMenuItemInfo(var text: String) {
	var resIcon: Int = 0
	var drawable: Drawable? = null
	var cmd: String = text
	var index: Int = -1
	var onClick: (String) -> Unit = {}
	var tintTheme: Boolean = true
	var hidden:Boolean = false
}