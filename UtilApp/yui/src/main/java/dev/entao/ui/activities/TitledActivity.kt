package dev.entao.ui.activities

import android.os.Bundle
import android.widget.LinearLayout
import dev.entao.ui.ext.LParam
import dev.entao.ui.ext.WidthFill
import dev.entao.ui.ext.backColorWhite
import dev.entao.ui.ext.height
import dev.entao.ui.viewcreator.createLinearVertical
import dev.entao.ui.widget.TitleBar

/**
 * Created by entaoyang@163.com on 16/4/14.
 */

abstract class TitledActivity : dev.entao.ui.activities.BaseActivity() {
	lateinit var rootView: LinearLayout
	lateinit var titleBar: TitleBar

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		rootView = this.createLinearVertical()
		rootView.backColorWhite()
		setContentView(rootView)
		titleBar = TitleBar(this)
		rootView.addView(titleBar, LParam.WidthFill.height(TitleBar.HEIGHT))

		onCreateContent(rootView)
		titleBar.commit()
	}


	abstract fun onCreateContent(contentView: LinearLayout)
}