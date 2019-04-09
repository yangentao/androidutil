@file:Suppress("unused")

package dev.entao.base

import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Label(val value: String)


@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Name(val value: String, val forDB: String = "", val forJson: String = "")

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultValue(val value: String)


//@FormOptions("0:OK","1:BAD")
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FormOptions(vararg val options: String)

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Required


inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation(): Boolean = null != this.findAnnotation<T>()

val KProperty<*>.nameProp: String
    get() {
        return this.findAnnotation<Name>()?.value ?: this.name
    }
val KProperty<*>.fullNameProp: String
    get() {
        val clsName = this.javaField?.declaringClass?.kotlin?.nameClass
        val fname = this.findAnnotation<Name>()?.value ?: this.name
        return clsName!! + "." + fname
    }

val KClass<*>.nameClass: String
    get() {
        return this.findAnnotation<Name>()?.value ?: this.simpleName!!
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

val KClass<*>.labelClass: String
    get() {
        return this.findAnnotation<Label>()?.value ?: this.simpleName!!
    }


val KProperty<*>.labelProp: String?
    get() {
        return this.findAnnotation<Label>()?.value
    }
val KProperty<*>.labelProp_: String
    get() {
        return this.findAnnotation<Label>()?.value ?: this.nameProp
    }

val KFunction<*>.labelFun: String?
    get() {
        return this.findAnnotation<Label>()?.value
    }
val KFunction<*>.labelFun_: String
    get() {
        return this.findAnnotation<Label>()?.value ?: this.nameFun
    }

val KProperty<*>.defaultValue: String?
    get() {
        return this.findAnnotation<DefaultValue>()?.value
    }




val KProperty0<*>.selectLabel: String
    get() {
        val map = this.selectOptionsStatic
        val v = this.getValue()?.toString() ?: ""
        return map[v] ?: ""
    }

val KProperty<*>.selectOptionsStatic: Map<String, String>
    get() {
        val fs = this.findAnnotation<FormOptions>() ?: return emptyMap()
        return FormSelectCache.find(fs)
    }

private object FormSelectCache {
    private val map = HashMap<FormOptions, LinkedHashMap<String, String>>()

    fun find(fs: FormOptions): Map<String, String> {
        val al = map[fs]
        if (al != null) {
            return al
        }

        val lMap = LinkedHashMap<String, String>()
        fs.options.forEach {
            val kv = it.split(":")
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