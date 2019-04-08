@file:Suppress("unused")

package dev.entao.ybase

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
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
