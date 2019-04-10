package dev.entao.ui.activities

import android.app.FragmentTransaction
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import android.widget.LinearLayout
import dev.entao.ui.ext.*
import dev.entao.ui.page.BaseFragment
import dev.entao.ui.util.FragmentHelper
import dev.entao.ui.viewcreator.createFrame
import dev.entao.ui.viewcreator.createLinearVertical
import dev.entao.ui.widget.TabBar
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
		fragMgr.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()
		rootView = this.createLinearVertical()
		this.setContentView(rootView)

		containerView = this.createFrame()
		rootView.addViewParam(containerView) {
			widthFill().heightDp(0).weight(1)
		}
		fragLayoutId = containerView.id

		fragmentHelper = FragmentHelper(fragMgr, fragLayoutId)

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
