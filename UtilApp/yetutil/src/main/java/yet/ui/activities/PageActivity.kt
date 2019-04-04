package yet.ui.activities

import android.app.FragmentTransaction
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.widget.FrameLayout
import yet.ui.page.BaseFragment
import yet.ui.viewcreator.createFrame
import yet.util.Msg

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


open class PageActivity : BaseActivity() {
	private lateinit var fragmentContainerView: FrameLayout
	var currentFragment: BaseFragment? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		currentFragment = Pages.onCreate(this)
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
		fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_NONE).commit()

		fragmentContainerView = this.createFrame()
		setContentView(fragmentContainerView)

		if (currentFragment != null) {
			replaceFragment(currentFragment!!)
		}
	}

	override fun onMsg(msg: Msg) {
		if (msg.isMsg(Pages.MSG_CLOSE_ALL_PAGES)) {
			finish()
			return
		}
		if (msg.isMsg(Pages.MSG_CLOSE_PAGE)) {
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
			Pages.removePage(p)
		}
		this.currentFragment = fragment
		fragmentManager.beginTransaction().replace(fragmentContainerId, fragment).commitAllowingStateLoss()
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		if (currentFragment?.onKeyDown(keyCode, event) == true) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}


}
