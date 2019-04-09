package dev.entao.ui.grid

import android.content.Context
import android.widget.LinearLayout
import dev.entao.ui.ext.HeightFlex
import dev.entao.ui.ext.LParam
import dev.entao.ui.ext.WidthFill
import dev.entao.ui.page.TitlePage
import dev.entao.ui.viewcreator.simpleGridView

/**
 * Created by entaoyang@163.com on 2016-08-24.
 * val p = GridSelectPage<String>()
 * p.title = "选择人员"
 * p.items = listOf("Yang", "En", "Tao", "Dou", "Ba", "YeYe")
 * p.bindRes {
 *     it to R.mipmap.ppp
 * }
 * p.onItemClick = { p, s ->
 *     logd(s)
 *     p.finish()
 * }
 * openPage(p)
 */

open class GridSelectPage : TitlePage() {
	lateinit var gridView: SimpleGridView
	var title: String = "选择"

	var onPageCreated: (GridSelectPage) -> Unit = {}


	override fun onContentCreated() {
		super.onContentCreated()
		onPageCreated(this)
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title(title)
		gridView = contentView.simpleGridView(LParam.WidthFill.HeightFlex) {

		}
	}


}