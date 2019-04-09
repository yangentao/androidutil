@file:Suppress("unused")

package dev.entao.appbase.sql

import dev.entao.base.fullNameProp
import dev.entao.base.nameClass
import dev.entao.base.nameProp
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Created by entaoyang@163.com on 2018-07-19.
 */

class SQLQuery {

    val ASC = "ASC"
    val DESC = "DESC"

    private val fromArr = arrayListOf<String>()
    private var whereStr: String = ""
    private var limitStr: String = ""
    private val selectArr: ArrayList<String> = arrayListOf()
    private val joinOnArr = arrayListOf<String>()
    private val orderArr = arrayListOf<String>()

    private var groupByStr: String = ""
    private var havingStr: String = ""
    private var distinct = false

    val args: ArrayList<Any> = ArrayList()

    val modelClassSet = HashSet<KClass<*>>()

    val sqlArgs: Array<String>
        get() {
            return args.map(Any::toString).toTypedArray()
        }

    fun groupBy(s: String): SQLQuery {
        groupByStr = s
        return this
    }

    fun groupBy(p: KProperty<*>): SQLQuery {
        groupByStr = p.nameProp
        return this
    }

    fun having(s: String): SQLQuery {
        havingStr = s
        return this
    }

    fun distinct(): SQLQuery {
        this.distinct = true
        return this
    }

    fun selectAll(): SQLQuery {
        selectArr.add("*")
        return this
    }

    fun select(vararg cols: KProperty<*>): SQLQuery {
        cols.mapTo(selectArr) { it.nameProp }
        return this
    }

    fun select(vararg cols: String): SQLQuery {
        selectArr.addAll(cols)
        return this
    }

    fun from(vararg clses: KClass<*>): SQLQuery {
        modelClassSet.addAll(clses)
        val ls = clses.map { it.nameClass }
        for (a in ls) {
            if (a !in fromArr) {
                fromArr += a
            }
        }
        return this
    }

    fun from(vararg tables: String): SQLQuery {
        fromArr.addAll(tables.map { "`$it`" })
        return this
    }

    fun joinOn(joinKClass: KClass<*>, cond: OnCond): SQLQuery {
        modelClassSet.add(joinKClass)
        joinOnArr.add("JOIN ${joinKClass.nameClass} ON ${cond.value}")
        return this
    }

    fun where(block: () -> Where): SQLQuery {
        val w = block.invoke()
        return where(w)
    }

    fun where(w: Where?): SQLQuery {
        if (w != null) {
            whereStr = w.value
            args.addAll(w.args)
        }
        return this
    }

    fun where(w: String, vararg params: Any) {
        whereStr = w
        args.addAll(params)
    }

    fun orderBy(ob: OrderBy?): SQLQuery {
        if (ob != null) {
            orderArr.addAll(ob.orderArr)
        }
        return this
    }

    fun orderBy(block: OrderBy.() -> Unit): SQLQuery {
        val ob = OrderBy()
        ob.block()
        return orderBy(ob)
    }

    fun orderBy(p: KProperty<*>, ascDesc: String): SQLQuery {
        orderArr.add(p.nameProp + " $ascDesc")
        return this
    }

    fun orderBy(col: String, ascDesc: String): SQLQuery {
        orderArr.add("$col $ascDesc")
        return this
    }

    fun asc(col: String): SQLQuery {
        orderArr.add("$col ASC")
        return this
    }

    fun desc(col: String): SQLQuery {
        orderArr.add("$col DESC")
        return this
    }

    fun asc(p: KProperty<*>): SQLQuery {
        return asc(p.nameProp)
    }

    fun desc(p: KProperty<*>): SQLQuery {
        return desc(p.nameProp)
    }

    fun limit(limit: Int): SQLQuery {
        if (limit > 0) {
            limitStr = "$limit "
        }
        return this
    }

    fun limit(limit: Int, offset: Int): SQLQuery {
        if (limit > 0 && offset >= 0) {
            limitStr = "$limit OFFSET $offset "
        }
        return this
    }

    fun toCountSQL(): String {
        val sb = StringBuilder(256)

        val dist = if (distinct) {
            " DISTINCT "
        } else {
            ""
        }

        if (selectArr.isEmpty()) {
            sb.append("SELECT COUNT($dist *) ").append(" ")
        } else {
            sb.append("SELECT COUNT($dist " + selectArr.joinToString(",")).append(") ")
        }
        sb.append("FROM ").append(fromArr.joinToString(",")).append(" ")

        if (joinOnArr.isNotEmpty()) {
            sb.append(joinOnArr.joinToString(" ")).append(" ")
        }

        if (whereStr.isNotEmpty()) {
            sb.append("WHERE ").append(whereStr).append(" ")
        }
        return sb.toString()
    }

    fun toSQL(): String {
        val dist = if (distinct) {
            " DISTINCT "
        } else {
            ""
        }
        val sb = StringBuilder(256)
        if (selectArr.isEmpty()) {
            sb.append("SELECT  $dist * ").append(" ")
        } else {
            sb.append("SELECT  $dist " + selectArr.joinToString(",")).append(" ")
        }
        sb.append("FROM ").append(fromArr.joinToString(",")).append(" ")
        if (joinOnArr.isNotEmpty()) {
            sb.append(joinOnArr.joinToString(" ")).append(" ")
        }


        if (whereStr.isNotEmpty()) {
            sb.append("WHERE ").append(whereStr).append(" ")
        }
        if (groupByStr.isNotEmpty()) {
            sb.append("GROUP BY ").append(groupByStr).append(" ")
            if (havingStr.isNotEmpty()) {
                sb.append("HAVING ").append(havingStr).append(" ")
            }
        }
        if (orderArr.isNotEmpty()) {
            sb.append("ORDER BY ").append(orderArr.joinToString(",")).append(" ")
        }
        if (limitStr.isNotEmpty()) {
            sb.append("LIMIT ").append(limitStr).append(" ")
        }
        return sb.toString()
    }

}


class OnCond(val value: String) {
    override fun toString(): String {
        return value
    }
}

infix fun KProperty<*>.EQ(value: KProperty<*>): OnCond {
    val s = this.fullNameProp
    val s2 = value.fullNameProp
    return OnCond("$s=$s2")
}

infix fun KProperty<*>.AS(value: String): String {
    return "${this.nameProp} AS $value"
}


class OrderBy {

    val orderArr = arrayListOf<String>()

    fun asc(p: KProperty<*>): OrderBy {
        orderArr.add(p.nameProp + " ASC")
        return this
    }

    fun desc(p: KProperty<*>): OrderBy {
        orderArr.add(p.nameProp + " DESC")
        return this
    }

    fun orderBy(col: String, ascDesc: String): OrderBy {
        orderArr.add("$col $ascDesc")
        return this
    }

    fun asc(col: String): OrderBy {
        orderArr.add("$col ASC")
        return this
    }

    fun desc(col: String): OrderBy {
        orderArr.add("$col DESC")
        return this
    }
}