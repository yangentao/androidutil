package yet.util

import dev.entao.log.logd
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Created by entaoyang@163.com on 16/5/14.
 */


class KRef(val cls: KClass<*>) {

	fun dump() {
		logd("DumpClass:", cls.simpleName)
		logd("Name:", cls.qualifiedName)
		logd("objectInstance:", cls.objectInstance)


		for (f in cls.constructors) {

		}

		for (m in cls.members) {
			dump(m, "    ")

		}
	}

	fun dump(c: KCallable<*>, prefix: String = "") {
		logd(prefix, "name:", c.name)
		logd(prefix, "returnType:", c.returnType)
		for (p in c.parameters) {
			dump(p, "    " + prefix)
		}
	}

	fun dump(p: KParameter, prefix: String = "") {
		logd(prefix, "index:", p.index)
		logd(prefix, "name:", p.name)
		logd(prefix, "isOptional:", p.isOptional)
		logd(prefix, "kind:", p.kind)
		logd(prefix, "type:", p.type)
		logd(prefix, "type:", p.annotations)
	}
}