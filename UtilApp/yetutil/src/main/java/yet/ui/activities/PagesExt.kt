package yet.ui.activities

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import yet.ui.page.BaseFragment
import kotlin.reflect.KClass

fun Activity.openPage(page: BaseFragment, block: Intent.() -> Unit = {}) {
	Pages.open(this, page, block)
}

fun Activity.openPage(cls: KClass<out BaseFragment>, block: Intent.() -> Unit = {}) {
	Pages.open(this, cls, block)
}

fun Fragment.openPage(page: BaseFragment, block: Intent.() -> Unit = {}) {
	Pages.open(this.activity, page, block)
}

fun Fragment.openPage(cls: KClass<out BaseFragment>, block: Intent.() -> Unit = {}) {
	Pages.open(this.activity, cls, block)
}