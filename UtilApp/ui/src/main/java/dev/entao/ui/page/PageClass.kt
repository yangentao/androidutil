package dev.entao.ui.page

import android.app.Activity
import dev.entao.ui.activities.openPage
import kotlin.reflect.full.createInstance
/**
 * Created by entaoyang@163.com on 2018-07-13.
 */

open class PageClass<T : BaseFragment> {
	val pageClass = javaClass.enclosingClass.kotlin

	@Suppress("UNCHECKED_CAST")
	fun open(parentPage: BaseFragment, block: T.() -> Unit = {}) {
		val p = pageClass.createInstance() as T
		p.block()
		parentPage.openPage(p)
	}

	@Suppress("UNCHECKED_CAST")
	fun open(act: Activity, block: T.() -> Unit = {}) {
		val p = pageClass.createInstance() as T
		p.block()
		act.openPage(p)
	}
}