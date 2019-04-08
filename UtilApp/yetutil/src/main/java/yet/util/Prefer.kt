package yet.util

import android.content.SharedPreferences
import dev.entao.appbase.App

/**
 *
 */
class Prefer(name: String) {

	private var sp: SharedPreferences = App.inst.getSharedPreferences(name, 0)

	fun edit(block: SharedPreferences.Editor.() -> Unit): Boolean {
		val a = sp.edit()
		a.block()
		return a.commit()
	}

	fun contains(key: String): Boolean {
		return sp.contains(key)
	}

	@Synchronized
	fun setIfNotPresent(key: String, value: Boolean): Boolean {
		val exist = sp.contains(key)
		if (!exist) {
			sp.edit().putBoolean(key, value).apply()
		}
		return exist
	}


	fun getBool(key: String, defValue: Boolean): Boolean {
		return sp.getBoolean(key, defValue)
	}


	fun getInt(key: String, defValue: Int): Int {
		return sp.getInt(key, defValue)
	}


	fun getLong(key: String, defValue: Long): Long {
		return sp.getLong(key, defValue)
	}

	fun getString(key: String): String? {
		return sp.getString(key, null)
	}

	fun getString(key: String, defValue: String): String? {
		return sp.getString(key, defValue)
	}


	fun getFloat(key: String, defValue: Float): Float {
		return sp.getFloat(key, defValue)
	}

	companion object {
		val G: Prefer by lazy { Prefer("global_prefer") }
	}

}
