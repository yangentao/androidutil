@file:Suppress("unused")

package dev.entao.sql

import dev.entao.yson.*
import kotlin.reflect.KClass

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



@Suppress("UNCHECKED_CAST")
fun <T> KClass<*>.createYsonModel(argValue: YsonObject): T {
	val c =
		this.constructors.first { it.parameters.size == 1 && it.parameters.first().type.classifier == YsonObject::class }
	return c.call(argValue) as T
}