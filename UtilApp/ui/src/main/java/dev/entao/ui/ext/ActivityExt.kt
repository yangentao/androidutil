package dev.entao.ui.ext

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 16/5/23.
 */
fun Context.openActivity(n: Intent) {
	try {
		this.startActivity(n)
	} catch (ex: Exception) {
		ex.printStackTrace()
	}
}


fun Fragment.openActivity(cls: KClass<out Activity>, block: Intent.() -> Unit = {}) {
	val n = Intent(activity, cls.java)
	n.block()
	activity.openActivity(n)
}

fun Context.openActivity(cls: KClass<out Activity>, block: Intent.() -> Unit = {}) {
	val n = Intent(this, cls.java)
	n.block()
	this.openActivity(n)
}

fun Fragment.openActivity(cls: Class<out Activity>, block: Intent.() -> Unit = {}) {
	val n = Intent(activity, cls)
	n.block()
	activity.openActivity(n)
}

fun Context.openActivity(cls: Class<out Activity>, block: Intent.() -> Unit = {}) {
	val n = Intent(this, cls)
	n.block()
	this.openActivity(n)
}


fun Activity.hasPermission(p: String): Boolean {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		this.checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED
	} else {
		true
	}
}


fun Context.viewUrl(uri: Uri) {
	val it = Intent(Intent.ACTION_VIEW, uri)
	it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
	openActivity(it)
}

fun Context.openApk(uri: Uri) {
	try {
		val i = Intent(Intent.ACTION_VIEW)
		i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		i.setDataAndType(uri, "application/vnd.android.package-archive")
		startActivity(i)
	} catch (e: Exception) {
		this.viewUrl(uri)
	}

}