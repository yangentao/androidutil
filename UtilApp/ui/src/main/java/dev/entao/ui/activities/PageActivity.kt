package dev.entao.ui.activities

import android.app.FragmentTransaction
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import dev.entao.ui.ext.fragMgr
import dev.entao.ui.page.BaseFragment
import dev.entao.ui.viewcreator.createFrame
import dev.entao.util.Msg

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


open class PageActivity : BaseActivity() {
	private lateinit var fragmentContainerView: FrameLayout
	var currentFragment: BaseFragment? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		currentFragment = dev.entao.ui.activities.Pages.onCreate(this)
		if (currentFragment == null) {
			currentFragment = getInitPage()
		}
		val full = currentFragment?.fullScreen ?: false
		if (full) {
			this.setWindowFullScreen()
		}

		val wColor: Int? = currentFragment?.windowBackColor
		if (wColor != null) {
			window.setBackgroundDrawable(ColorDrawable(wColor))
		}
		fragMgr.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()

		fragmentContainerView = this.createFrame()
		setContentView(fragmentContainerView)

		if (currentFragment != null) {
			replaceFragment(currentFragment!!)
		}
	}

	override fun onMsg(msg: Msg) {
		if (msg.isMsg(dev.entao.ui.activities.Pages.MSG_CLOSE_ALL_PAGES)) {
			finish()
			return
		}
		if (msg.isMsg(dev.entao.ui.activities.Pages.MSG_CLOSE_PAGE)) {
			val frag = currentFragment ?: return
			if (frag::class == msg.cls) {
				finish()
			}
			return
		}
		super.onMsg(msg)
	}

	open fun getInitPage(): BaseFragment? {
		return null
	}

	override fun finish() {
		super.finish()
		val ac = currentFragment?.activityAnim
		if (ac != null) {
			this.overridePendingTransition(ac.finishEnter, ac.finishExit)
		}

	}

	override fun onBackPressed() {
		if (currentFragment?.onBackPressed() == true) {
			return
		}
		super.onBackPressed()
	}

	private val fragmentContainerId: Int
		get() = fragmentContainerView.id

	fun replaceFragment(fragment: BaseFragment) {
		val p = currentFragment
		if (p != null) {
			dev.entao.ui.activities.Pages.removePage(p)
		}
		this.currentFragment = fragment
		fragMgr.beginTransaction().replace(fragmentContainerId, fragment).commitAllowingStateLoss()
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (currentFragment?.onKeyDown(keyCode, event) == true) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}


}
