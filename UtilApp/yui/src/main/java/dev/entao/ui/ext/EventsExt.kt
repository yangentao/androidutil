package dev.entao.ui.ext

import android.view.View
import android.widget.Switch


/**
 * Created by yangentao on 2015/11/21.
 * entaoyang@163.com
 */

@Suppress("UNCHECKED_CAST")
fun <T : View> T.onClick(block: (view: T) -> Unit): T {
	this.setOnClickListener {
		block.invoke(it as T)
	};
	return this
}

fun <T : Switch> T.onCheckChanged(block: (Switch, Boolean) -> Unit): T {
	this.setOnCheckedChangeListener { view, check ->
		block.invoke(view as Switch, check)
	}
	return this
}