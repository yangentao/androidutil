package dev.entao.ui.activities

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import dev.entao.ui.page.BaseFragment
import kotlin.reflect.KClass

fun Activity.openPage(page: BaseFragment, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this, page, block)
}

fun Activity.openPage(cls: KClass<out BaseFragment>, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this, cls, block)
}

fun Fragment.openPage(page: BaseFragment, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this.activity, page, block)
}

fun Fragment.openPage(cls: KClass<out BaseFragment>, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this.activity, cls, block)
}