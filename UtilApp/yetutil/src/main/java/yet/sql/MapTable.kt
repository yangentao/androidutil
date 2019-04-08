package yet.sql

import dev.entao.yapp.App
import dev.entao.ybase.defaultValue
import dev.entao.ybase.nameProp
import dev.entao.yog.logd
import dev.entao.yson.YsonArray
import dev.entao.yson.YsonObject
import yet.ext.defaultValueOfProperty
import yet.ext.strToV
import yet.util.Task
import java.util.*
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2017-03-24.
 */

class MapTable(val table: String) {

	init {
		Task.onceProcess("maptable.$table") {
			liteBase.createTable(table, "$KEY TEXT PRIMARY KEY", "$VAL TEXT")
			liteBase.createIndex(table, "value")
		}
	}

	operator fun <V> setValue(thisRef: Any?, property: KProperty<*>, value: V) {
		this.put(property.nameProp, value)
	}

	@Suppress("UNCHECKED_CAST")
	operator fun <V> getValue(thisRef: Any?, property: KProperty<*>): V {
		val v = get(property.nameProp) ?: property.defaultValue
		?: return if (property.returnType.isMarkedNullable) {
			null as V
		} else {
			defaultValueOfProperty(property)
		}
		return strToV(v, property)
	}

	fun trans(block: (MapTable) -> Unit) {
		liteBase.trans {
			block(this@MapTable)
		}
	}

	fun toHashMap(): HashMap<String, String> {
		val map = HashMap<String, String>(512)
		toMap(map)
		return map
	}

	fun toMap(map: MutableMap<String, String>) {
		val c = liteBase.query("select $KEY, $VAL from $table", emptyList()) ?: return
		c.listRow_.forEach {
			val k = it.str(KEY)!!
			val v = it.str(VAL)?: ""
			map[k] = v
		}
	}

	fun putAll(map: Map<String, String>) {
		this.trans {
			for ((k, v) in map) {
				liteBase.replaceX(table, "key" to k, "value" to v)
			}
		}

	}

	fun findKey(value:String):String? {
		val c = liteBase.query("select key from $table where value = ? limit 1", listOf(value)) ?: return null
		val m = c.listRow_.firstOrNull() ?: return null
		return m.str(KEY)
	}

	fun has(key: String): Boolean {
		val c = liteBase.query("select value from $table where key=? limit 1", listOf(key))
				?: return false
		return c.listRow_.isNotEmpty()
	}

	operator fun get(key: String): String? {
		val c = liteBase.query("select value from $table where key=? limit 1", listOf(key))
				?: return null
		val m = c.listRow_.firstOrNull() ?: return null
		return m.str(VAL)
	}

	operator fun set(key: String, value: String?) {
		liteBase.replaceX(table, "key" to key, "value" to (value ?: ""))
	}

	fun getString(key: String): String? {
		return get(key)
	}

	fun putString(key: String, value: String?) {
		set(key, value)
	}

	fun put(key: String, value: Any?) {
		return set(key, value?.toString())
	}

	fun getInt(key: String): Int? {
		return getString(key)?.toIntOrNull()
	}

	fun getLong(key: String): Long? {
		return getString(key)?.toLongOrNull()
	}

	fun getDouble(key: String): Double? {
		return getString(key)?.toDoubleOrNull()
	}

	fun getBool(key: String): Boolean? {
		return getString(key)?.toBoolean()
	}

	fun getYsonObject(key: String): YsonObject? {
		val s = this.getString(key) ?: return null
		return YsonObject(s)
	}

	fun getYsonArray(key: String): YsonArray? {
		val s = this.getString(key) ?: return null
		return YsonArray(s)
	}

	fun remove(key: String): Int {
		return liteBase.delete(table, "$KEY = ?", arrayOf(key))
	}

	fun removeAll(): Int {
		return liteBase.delete(table, null, null)
	}

	fun dumpAll() {
		val map = toHashMap()
		for ((k, v) in map) {
			logd(k, " = ", v)
		}

	}

	companion object {
		const val KEY = "key"
		const val VAL = "value"

		private var liteBase = App.openOrCreateDatabase("maptable.db")

		val config = MapTable("global_config_map_table")
	}
}
