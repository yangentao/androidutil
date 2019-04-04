package yet.anno

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-04-06.
 */


val KClass<*>.nameClass: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.simpleName!!
	}

val KClass<*>.nameClassSQL: String
	get() {
		return "`" + this.nameClass + "`"
	}

val KClass<*>.labelClass: String
	get() {
		return this.findAnnotation<Label>()?.value ?: this.simpleName!!
	}

val KClass<*>.autoAlterTable: Boolean
	get() {
		return this.findAnnotation<AutoAlterTable>()?.value ?: true
	}


val KProperty<*>.fullNameProp: String
	get() {
		var tabName = this.javaField?.declaringClass?.kotlin?.nameClass
		val fname = this.findAnnotation<Name>()?.value ?: this.name
		return tabName!! + "." + fname
	}
val KProperty<*>.fullNamePropSQL: String
	get() {
		var tabName = this.javaField?.declaringClass?.kotlin?.nameClass
		val fname = this.findAnnotation<Name>()?.value ?: this.name
		return "`" + tabName!! + "`.`" + fname + "`"
	}

val KProperty<*>.nameProp: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.name
	}

val KParameter.nameParam: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.name
		?: throw IllegalStateException("参数没有名字")
	}

val KFunction<*>.nameFun: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.name
	}

val KProperty<*>.isExcluded: Boolean
	get() {
		return this.findAnnotation<Exclude>() != null
	}
val KProperty<*>.isPrimaryKey: Boolean
	get() {
		return this.findAnnotation<PrimaryKey>() != null
	}

val KProperty<*>.defaultValue: String?
	get() {
		return this.findAnnotation<DefaultValue>()?.value
	}

val KProperty<*>.labelProp: String?
	get() {
		return this.findAnnotation<Label>()?.value
	}
val KProperty<*>.labelProp_: String
	get() {
		return this.findAnnotation<Label>()?.value ?: this.nameProp
	}


inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation(): Boolean = null != this.findAnnotation<T>()

val KFunction<*>.labelFun: String?
	get() {
		return this.findAnnotation<Label>()?.value
	}
val KFunction<*>.labelFun_: String
	get() {
		return this.findAnnotation<Label>()?.value ?: this.nameFun
	}




val KProperty<*>.selectOptionsStatic: Map<String, String>
	get() {
		val fs = this.findAnnotation<FormSelect>() ?: return emptyMap()
		return FormSelectCache.find(fs)
	}

private object FormSelectCache {
	private val map = HashMap<FormSelect, LinkedHashMap<String, String>>()

	fun find(fs: FormSelect): Map<String, String> {
		val al = map[fs]
		if (al != null) {
			return al
		}

		val lMap = LinkedHashMap<String, String>()
		val items = fs.value.split(fs.itemSep)
		items.forEach {
			val kv = it.split(fs.keyValueSep)
			if (kv.size == 2) {
				lMap[kv[0]] = kv[1]
			} else if (kv.size == 1) {
				lMap[kv[0]] = kv[0]
			}
		}
		map[fs] = lMap
		return lMap
	}
}