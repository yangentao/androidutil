package dev.entao.ui.activities.drawer

import android.app.FragmentTransaction
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import dev.entao.ui.R
import dev.entao.ui.ext.*
import dev.entao.ui.page.BaseFragment
import dev.entao.ui.util.FragmentHelper
import dev.entao.ui.viewcreator.createFrame
import dev.entao.ui.viewcreator.createLinearVertical
import dev.entao.ui.widget.Action
import java.util.*

open class DrawerActivity : dev.entao.ui.activities.BaseActivity() {
	lateinit var drawerLayout: DrawerLayout
	lateinit var rootLinearLayout: LinearLayout
		private set
	lateinit var containerFrameLayout: FrameLayout
		private set
	val containerId: Int get() = containerFrameLayout.id

	lateinit var fragmentHelper: FragmentHelper
	lateinit var navView: DrawerNavView

	@Suppress("DEPRECATION")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		fragMgr.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()

		drawerLayout = DrawerLayout(this).genId()

		rootLinearLayout = createLinearVertical()
		val lp = DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
		drawerLayout.addView(rootLinearLayout, lp)

		navView = DrawerNavView(this)
		navView.onActionCallback = {
			closeDrawer()
		}
		val lp2 = DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
		lp2.gravity = Gravity.START
		drawerLayout.addView(navView, lp2)

		this.setContentView(drawerLayout)

		val toggle = ActionBarDrawerToggle(
				this, drawerLayout, R.string.yet_navigation_drawer_open, R.string.yet_navigation_drawer_close)
		drawerLayout.setDrawerListener(toggle)
		toggle.syncState()
		containerFrameLayout = createFrame()
		rootLinearLayout.addViewParam(containerFrameLayout) {
			widthFill().height(0).weight(1f)
		}
		fragmentHelper = FragmentHelper(fragMgr, containerId)
	}

	override fun onPostCreate(savedInstanceState: Bundle?) {
		super.onPostCreate(savedInstanceState)
		commitDrawerActions()
	}

	fun replaceFragment(frag: BaseFragment) {
		fragmentHelper.replace(frag, "main")
	}

	var navHeaderView: View
		get() = navView.header
		set(view) {
			navView.header = view
		}

	private val actions = ArrayList<Action>()

	fun addDrawerAction(a: Action):Action {
		actions.add(a)
		return a
	}

	fun addDrawerAction(titleAndTag: String): Action {
		val a = Action(titleAndTag)
		addDrawerAction(a)
		return a
	}

	fun commitDrawerActions() {
		if (actions.isNotEmpty()) {
			navView.setActions(actions)
			actions.clear()
		}
	}


	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		val currentFragment = fragmentHelper.currentBaseFragment
		if (currentFragment != null && currentFragment.onKeyDown(keyCode, event)) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}

	override fun finish() {
		val currentFragment = fragmentHelper.currentBaseFragment
		super.finish()
		if (currentFragment != null) {
			val ac = currentFragment.activityAnim
			if (ac != null) {
				this.overridePendingTransition(ac.finishEnter, ac.finishExit)
			}
		}
	}

	fun openDrawer() {
		drawerLayout.openDrawer(GravityCompat.START)
	}

	fun closeDrawer() {
		drawerLayout.closeDrawer(GravityCompat.START)
	}

	val isDrawerOpen: Boolean
		get() = drawerLayout.isDrawerOpen(GravityCompat.START)

	override fun onBackPressed() {
		if (isDrawerOpen) {
			closeDrawer()
			return
		}
		val currentFragment = fragmentHelper.currentBaseFragment
		if (currentFragment != null) {
			if (currentFragment.onBackPressed()) {
				return
			}
		}
		super.onBackPressed()
	}


}
