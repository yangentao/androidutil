package yet.ext

import yet.anno.Name
import yet.yson.YsonObject
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * Created by entaoyang@163.com on 2017-04-16.
 */

val KClass<*>.customName: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.simpleName!!
	}

inline fun <reified T : Any> KClass<T>.createInstance(argCls: KClass<*>, argValue: Any): T {
	val c = this.constructors.first { it.parameters.size == 1 && it.parameters.first().type.classifier == argCls }
	return c.call(argValue)
}

@Suppress("UNCHECKED_CAST")
fun <T> KClass<*>.createYsonModel(argValue: YsonObject): T {
	val c = this.constructors.first { it.parameters.size == 1 && it.parameters.first().type.classifier == YsonObject::class }
	return c.call(argValue) as T
}