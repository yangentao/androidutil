package dev.entao.util.app

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.Fragment
import dev.entao.ui.ext.act


fun Context.hasPerm(p: String): Boolean {
	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		PackageManager.PERMISSION_GRANTED == this.checkSelfPermission(p)
	} else {
		true
	}
}


fun Activity.needPerm(p: String, block: (Boolean) -> Unit) {
	val perm = Perm(hashSetOf(p))
	perm.onResult = {
		block(it[p] ?: true)
	}
	perm.req(this)
}

fun Activity.needPerm(pSet: Set<String>, block: (Map<String, Boolean>) -> Unit) {
	val perm = Perm(pSet.toHashSet())
	perm.onResult = {
		block(it)
	}
	perm.req(this)
}

fun Fragment.needPerm(p: String, block: (Boolean) -> Unit) {
	val perm = Perm(hashSetOf(p))
	perm.onResult = {
		block(it[p] ?: true)
	}
	perm.req(this)
}

fun Fragment.needPerm(pSet: Set<String>, block: (Map<String, Boolean>) -> Unit) {
	val perm = Perm(pSet.toHashSet())
	perm.onResult = {
		block(it)
	}
	perm.req(this)
}


fun Fragment.hasPerm(p: String): Boolean {
	return act.hasPerm(p)
}