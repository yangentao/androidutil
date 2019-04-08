package dev.entao.sql

import dev.entao.base.getValue
import dev.entao.base.nameClass
import dev.entao.base.nameProp
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaField


val KClass<*>.nameClassSQL: String
    get() {
        return "`" + this.nameClass + "`"
    }


val KClass<*>.autoAlterTable: Boolean
    get() {
        return this.findAnnotation<AutoAlterTable>()?.value ?: true
    }


val KProperty<*>.fullNamePropSQL: String
    get() {
        var tabName = this.javaField?.declaringClass?.kotlin?.nameClass
        val fname = this.nameProp
        return "`" + tabName!! + "`.`" + fname + "`"
    }


val KProperty<*>.isExcluded: Boolean
    get() {
        return this.findAnnotation<Exclude>() != null
    }
val KProperty<*>.isPrimaryKey: Boolean
    get() {
        return this.findAnnotation<PrimaryKey>() != null
    }


inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation(): Boolean = null != this.findAnnotation<T>()



val KProperty0<*>.selectLabel: String
    get() {
        val map = this.selectOptionsStatic
        val v = this.getValue()?.toString() ?: ""
        return map[v] ?: ""
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