package dev.entao.ui.activities

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import dev.entao.ui.ext.act
import dev.entao.ui.page.BaseFragment
import kotlin.reflect.KClass

fun Activity.openPage(page: BaseFragment, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this, page, block)
}

fun Activity.openPage(cls: KClass<out BaseFragment>, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this, cls, block)
}

fun Fragment.openPage(page: BaseFragment, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this.act, page, block)
}

fun Fragment.openPage(cls: KClass<out BaseFragment>, block: Intent.() -> Unit = {}) {
	dev.entao.ui.activities.Pages.open(this.act, cls, block)
}