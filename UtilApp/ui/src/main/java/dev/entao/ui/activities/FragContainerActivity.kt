package dev.entao.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import dev.entao.log.logd
import dev.entao.ui.R
import dev.entao.ui.ext.fragMgr
import dev.entao.ui.page.BaseFragment
import dev.entao.ui.viewcreator.createFrame

class FragContainerActivity : AppCompatActivity() {

	private lateinit var containerView: FrameLayout

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		containerView = this.createFrame()
		setContentView(containerView)
	}


	override fun finish() {
		super.finish()
		logd("MainActivity", "finish()")
	}


	override fun onBackPressed() {
		logd("MainActivity", "onBackPressed()")
		if ((fragMgr.fragments.lastOrNull() as? BaseFragment)?.onBackPressed() == true) {
			return
		}

		if (fragMgr.backStackEntryCount > 1) {
			fragMgr.popBackStack()
		} else {
			finish()
		}
	}

	private val containerId: Int
		get() = containerView.id


	fun push(fragment: BaseFragment) {
		val b = fragMgr.beginTransaction()
		if (fragMgr.backStackEntryCount > 0) {
			b.setCustomAnimations(R.animator.yet_right_in, R.animator.yet_fade_out, R.animator.yet_fade_in, R.animator.yet_right_out)
		}
		b.add(containerId, fragment)
		b.addToBackStack(null)
		b.commitAllowingStateLoss()
	}

	fun pop() {
		if (fragMgr.backStackEntryCount > 1) {
			fragMgr.popBackStack()
		} else {
			finish()
		}
	}

	fun popToBottom() {
		while (fragMgr.backStackEntryCount > 1) {
			fragMgr.popBackStackImmediate()
		}
	}

}
