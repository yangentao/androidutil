package yet.ext

import yet.anno.*
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField

/**
 * Created by entaoyang@163.com on 2017-03-13.
 */


typealias Prop = KProperty<*>

typealias Prop0 = KProperty0<*>
typealias Prop1 = KProperty1<*, *>

/*
 * class A{
 *      val set:HashSet<String> = HashSet()
 * }
 *
 * A::set.firstGenericType => String::class
 *
 */
val KProperty<*>.firstGenericType: KClass<*>? get() = this.returnType.arguments.firstOrNull()?.type?.classifier as? KClass<*>


val KProperty<*>.isTypeString: Boolean get() = this.returnType.isTypeString
val KProperty<*>.isTypeInt: Boolean get() = this.returnType.isTypeInt
val KProperty<*>.isTypeLong: Boolean get() = this.returnType.isTypeLong
val KProperty<*>.isTypeByte: Boolean get() = this.returnType.isTypeByte
val KProperty<*>.isTypeShort: Boolean get() = this.returnType.isTypeShort
val KProperty<*>.isTypeChar: Boolean get() = this.returnType.isTypeChar
val KProperty<*>.isTypeBoolean: Boolean get() = this.returnType.isTypeBoolean
val KProperty<*>.isTypeFloat: Boolean get() = this.returnType.isTypeFloat
val KProperty<*>.isTypeDouble: Boolean get() = this.returnType.isTypeDouble
val KProperty<*>.isTypeByteArray: Boolean get() = this.returnType.isTypeByteArray
val KProperty<*>.isTypeHashSet: Boolean get() = this.returnType.isClass(HashSet::class)
val KProperty<*>.isTypeArrayList: Boolean get() = this.returnType.isClass(ArrayList::class)

fun KProperty<*>.isClass(cls: KClass<*>): Boolean {
	return this.returnType.isClass(cls)
}

val KProperty<*>.isTypeEnum: Boolean
	get() {
		return this.javaField?.type?.isEnum ?: false
	}

fun KProperty<*>.getValue(): Any? {
	if (this.getter.parameters.isEmpty()) {
		return this.getter.call()
	}
	return null
}

fun KProperty<*>.getValue(inst: Any): Any? {
	if (this.getter.parameters.isEmpty()) {
		return this.getter.call()
	}
	return this.getter.call(inst)
}

fun KProperty<*>.getBindValue(): Any? {
	if (this.getter.parameters.isEmpty()) {
		return this.getter.call()
	}
	return null
}

fun KMutableProperty<*>.setValue(inst: Any, value: Any?) {
	this.setter.call(inst, value)
}

val KProperty<*>.isPublic: Boolean get() = this.visibility == KVisibility.PUBLIC


val KProperty<*>.customNamePrefixClass: String
	get() {
		var tabName = this.javaField?.declaringClass?.kotlin?.customName
		return tabName!! + "." + this.customName
	}

val KProperty<*>.customName: String
	get() {
		return this.findAnnotation<Name>()?.value ?: this.name
	}


val KProperty<*>.isExcluded: Boolean
	get() {
		return this.hasAnnotation<Exclude>()
	}
val KMutableProperty<*>.isPrimaryKey: Boolean
	get() {
		return this.hasAnnotation<PrimaryKey>()
	}

val KProperty<*>.defaultValue: String?
	get() {
		return this.findAnnotation<DefaultValue>()?.value
	}

val KProperty<*>.labelValue: String?
	get() {
		return this.findAnnotation<Label>()?.value
	}
val KProperty<*>.labelValue_: String
	get() {
		return this.labelValue!!
	}


@Suppress("UNCHECKED_CAST")
fun <V> strToV(v: String, property: KProperty<*>): V {
	val retType = property.returnType
	if (retType.isTypeString) {
		return v as V
	}
	val c = TextConverts[retType.classifier]
	if (c != null) {
		return c.fromText(v) as V
	}
	throw IllegalArgumentException("不支持的类型${property.fullNameProp}")
}

@Suppress("UNCHECKED_CAST")
fun <V> defaultValueOfProperty(p: KProperty<*>): V {
	val retType = p.returnType

	val c = TextConverts[retType.classifier]
	if (c != null) {
		return c.defaultValue as V
	}

	throw IllegalArgumentException("不支持的类型modelMap: ${p.fullNameProp}")
}


val KProperty0<*>.selectLabel: String
	get() {
		val map = this.selectOptionsStatic
		val v = this.getValue()?.toString() ?: ""
		return map[v] ?: ""
	}

