package yet.ext

import dev.entao.ybase.getValue
import yet.anno.Exclude
import yet.anno.PrimaryKey
import yet.anno.hasAnnotation
import yet.anno.selectOptionsStatic
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

/**
 * Created by entaoyang@163.com on 2017-03-13.
 */


val KProperty<*>.isExcluded: Boolean
    get() {
        return this.hasAnnotation<Exclude>()
    }
val KMutableProperty<*>.isPrimaryKey: Boolean
    get() {
        return this.hasAnnotation<PrimaryKey>()
    }




val KProperty0<*>.selectLabel: String
    get() {
        val map = this.selectOptionsStatic
        val v = this.getValue()?.toString() ?: ""
        return map[v] ?: ""
    }

