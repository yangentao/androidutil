package yet.ui.activities

import android.app.FragmentTransaction
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import yet.ui.ext.addViewParam
import yet.ui.ext.heightDp
import yet.ui.ext.weight
import yet.ui.ext.widthFill
import yet.ui.page.BaseFragment
import yet.ui.util.FragmentHelper
import yet.ui.viewcreator.createFrame
import yet.ui.viewcreator.createLinearVertical
import yet.ui.widget.TabBar
import java.util.*
import kotlin.collections.set

/**
 * Created by entaoyang@163.com on 16/3/13.
 */

open class TabBarActivity : BaseActivity() {
	lateinit var rootView: LinearLayout
		private set
	lateinit var containerView: FrameLayout
		private set
	private var fragLayoutId = 0
	lateinit var tabBar: TabBar
		private set
	private val pages = HashMap<String, BaseFragment>()

	lateinit var fragmentHelper: FragmentHelper

	fun selectTab(tag: String) {
		tabBar.select(tag, true)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()
		rootView = this.createLinearVertical()
		this.setContentView(rootView)

		containerView = this.createFrame()
		rootView.addViewParam(containerView) {
			widthFill().heightDp(0).weight(1f)
		}
		fragLayoutId = containerView.id

		fragmentHelper = FragmentHelper(fragmentManager, fragLayoutId)

		tabBar = TabBar(this)

		tabBar.onSelect = {
			this@TabBarActivity.onXTabBarSelect(it.text)
		}

		rootView.addView(tabBar)
	}

	fun tab(text: String, icon: Int, page: BaseFragment) {
		tabBar.tab(text, icon)
		pages[text] = page
		page.activityAnim = null
	}

	fun tab(text: String, drawable: Drawable, page: BaseFragment) {
		tabBar.tab(text, drawable)
		pages[text] = page
		page.activityAnim = null
	}

	fun onXTabBarSelect(text: String) {
		val page = pages[text]
		fragmentHelper.showFragment(page!!, text)
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		val currentFragment = fragmentHelper.currentBaseFragment
		if (currentFragment != null && currentFragment.onKeyDown(keyCode, event)) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}

	override fun finish() {
//		val currentFragment = fragmentHelper.currentBaseFragment
		super.finish()
//		if (currentFragment != null) {
//			val ac = currentFragment.activityAnim
//			if (ac != null) {
//				this.overridePendingTransition(ac.finishEnter, ac.finishExit)
//			}
//		}
	}

	override fun onBackPressed() {
		val currentFragment = fragmentHelper.currentBaseFragment
		if (currentFragment != null) {
			if (currentFragment.onBackPressed()) {
				return
			}
		}
		super.onBackPressed()
	}
}
