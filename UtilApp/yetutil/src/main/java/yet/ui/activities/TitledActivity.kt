package yet.ui.activities

import android.os.Bundle
import android.widget.LinearLayout
import yet.ui.ext.LParam
import yet.ui.ext.*
import yet.ui.ext.backColorWhite
import yet.ui.viewcreator.createLinearVertical
import yet.ui.widget.TitleBar

/**
 * Created by entaoyang@163.com on 16/4/14.
 */

abstract class TitledActivity : BaseActivity() {
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