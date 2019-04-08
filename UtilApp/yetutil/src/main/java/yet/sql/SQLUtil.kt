package yet.sql

import yet.yson.*

/**
 * Created by entaoyang@163.com on 2018-07-19.
 */



fun stringAnyMapToYson(map: Map<String, Any?>): YsonObject {
	val yo = YsonObject()
	map.forEach {
		yo.any(it.key, it.value)
	}
	return yo
}

fun ysonToMap(yo: YsonObject, map: MutableMap<String, Any?>) {
	yo.forEach {
		val v = it.value
		val vv: Any? = when (v) {
			is YsonNull -> null
			is YsonString -> v.data
			is YsonNum -> v.data
			is YsonObject -> v
			is YsonArray -> v
			else -> v
		}
		map[it.key] = vv
	}
}