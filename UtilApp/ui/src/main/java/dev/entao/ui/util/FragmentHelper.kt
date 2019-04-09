package dev.entao.ui.util

import android.app.Fragment
import android.app.FragmentManager
import dev.entao.log.Yog
import dev.entao.ui.page.BaseFragment

/**
 * Created by yet on 2015/10/10.
 */
class FragmentHelper(val fm: FragmentManager, val frameLayoutId: Int) {
	var current: Fragment? = null
		private set

	val currentBaseFragment: BaseFragment?
		get() {
			if (current is BaseFragment) {
				return current as BaseFragment
			}
			return null
		}

	fun find(tag: String): Fragment? {
		return fm.findFragmentByTag(tag)
	}

	fun replace(fragment: Fragment, tag: String) {
		this.current = fragment
		fm.beginTransaction().replace(frameLayoutId, fragment, tag).commit()
	}

	fun replace(fragment: Fragment, tag: String, inAnim: Int, outAnim: Int) {
		this.current = fragment
		val ft = fm.beginTransaction()
		ft.setCustomAnimations(inAnim, outAnim)
		ft.replace(frameLayoutId, fragment, tag)
		ft.commit()
	}

	fun add(fragment: Fragment, tag: String) {
		Yog.d("addFragment: ", fragment.javaClass.simpleName)

		val old = find(tag)
		if (old === fragment) {//已经添加过了, 不再添加, tag相同且fragment相同
			fm.beginTransaction().show(fragment).commit()
			current = fragment
			return
		}
		if (old != null) {
			Yog.e("already exist tag ", tag)
			return
		}

		val ft = fm.beginTransaction()
		if (current != null) {
			ft.hide(current)
		}
		ft.add(frameLayoutId, fragment, tag)
		ft.commit()
		current = fragment
	}

	fun showFragment(fragment: Fragment, tag: String) {
		val old = find(tag)
		if (old == null) {
			add(fragment, tag)
		} else {
			show(tag)
			if (old !== fragment) {
				Yog.e("show fragment with different fragment ")
			}
		}
	}

	fun show(tag: String) {
		val f = find(tag)
		if (f == null) {
			Yog.e("fragment no found by tag ", tag)
			return
		}
		val ft = fm.beginTransaction()
		if (current != null && current !== f) {
			ft.hide(current)
		}
		ft.show(f)
		ft.commit()
		current = f
	}

	fun hide(tag: String) {
		val f = find(tag)
		if (f == null) {
			Yog.e("fragment not found by tag", tag)
			return
		}
		fm.beginTransaction().hide(f).commit()
	}

	fun remove(tag: String) {
		val fragment = find(tag)
		if (fragment != null) {
			fm.beginTransaction().remove(fragment).commit()
			if (current === fragment) {
				current = null
			}
		}
	}
}
