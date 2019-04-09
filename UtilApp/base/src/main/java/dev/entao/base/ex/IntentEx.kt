@file:Suppress("unused")

package dev.entao.base.ex

import android.content.Intent

fun Intent.extraBool(key: String): Boolean {
	return this.extras?.getBoolean(key) ?: false
}

fun Intent.extraInt(key: String): Int {
	return this.extras?.getInt(key) ?: 0
}

fun Intent.extraLong(key: String): Long {
	return this.extras?.getLong(key) ?: 0L
}

fun Intent.extraString(key: String): String? {
	return this.extras?.getString(key)
}

fun Intent.extraDouble(key: String): Double {
	return this.extras?.getDouble(key) ?: 0.0
}

fun Intent.putBool(key: String, value: Boolean) {
	this.putExtra(key, value)
}

fun Intent.putInt(key: String, value: Int) {
	this.putExtra(key, value)
}

fun Intent.putLong(key: String, value: Long) {
	this.putExtra(key, value)
}

fun Intent.putDouble(key: String, value: Double) {
	this.putExtra(key, value)
}

fun Intent.putString(key: String, value: String) {
	this.putExtra(key, value)
}
