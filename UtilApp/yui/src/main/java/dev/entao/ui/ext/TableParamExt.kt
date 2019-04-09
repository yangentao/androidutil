package dev.entao.ui.ext

import android.widget.TableLayout
import android.widget.TableRow

/**
 * Created by entaoyang@163.com on 2016-08-03.
 */


val TParam: TableLayout.LayoutParams
	get() {
		return TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
	}
val RowParam: TableRow.LayoutParams
	get() {
		return TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
	}

fun TableRow.LayoutParams.span(n: Int): TableRow.LayoutParams {
	this.span = n
	return this
}

fun TableRow.LayoutParams.atColumn(n: Int): TableRow.LayoutParams {
	this.column = n
	return this
}