package yet.ext

import dev.entao.yson.YsonObject
import kotlin.reflect.KClass

/**
 * Created by entaoyang@163.com on 2017-04-16.
 */


inline fun <reified T : Any> KClass<T>.createInstance(argCls: KClass<*>, argValue: Any): T {
	val c = this.constructors.first { it.parameters.size == 1 && it.parameters.first().type.classifier == argCls }
	return c.call(argValue)
}

@Suppress("UNCHECKED_CAST")
fun <T> KClass<*>.createYsonModel(argValue: YsonObject): T {
	val c = this.constructors.first { it.parameters.size == 1 && it.parameters.first().type.classifier == YsonObject::class }
	return c.call(argValue) as T
}