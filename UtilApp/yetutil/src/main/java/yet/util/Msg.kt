package yet.util

import android.support.annotation.Keep
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

/**
 * Created by yangentao on 16/3/12.
 */

@Keep
class Msg(val msg: String) {
	var result = ArrayList<Any>()
	var n1: Long = 0
	var n2: Long = 0
	var s1: String = ""
	var s2: String = ""
	var b1: Boolean = false
	var b2: Boolean = false
	var cls: KClass<*>? = null

	constructor(cls: Class<*>) : this(cls.name)
	constructor(cls: KClass<*>) : this(cls.qualifiedName!!)

	override fun hashCode(): Int {
		return msg.hashCode()
	}

	override fun equals(other: Any?): Boolean {
		if (other is Msg) {
			return msg == other.msg &&
					n1 == other.n1 &&
					n2 == other.n2 &&
					s1 == other.s1 &&
					s2 == other.s2 &&
					b1 == other.b1 &&
					b2 == other.b2

		}
		return false
	}

	fun clazz(c: KClass<*>): Msg {
		this.cls = c
		return this
	}

	fun isMsg(vararg msgs: String): Boolean {
		return this.msg in msgs
	}

	fun isMsg(vararg classes: Class<*>): Boolean {
		return classes.find { this.msg == it.name } != null
	}

	fun isMsg(vararg classes: KClass<*>): Boolean {
		return classes.find { this.msg == it.qualifiedName } != null
	}

	operator fun contains(msg: String): Boolean {
		return isMsg(msg)
	}

	operator fun contains(msg: Class<*>): Boolean {
		return isMsg(msg)
	}

	operator fun contains(msg: KClass<*>): Boolean {
		return isMsg(msg)
	}

	fun argB(b1: Boolean, b2: Boolean = false): Msg {
		this.b1 = b1
		this.b2 = b2
		return this
	}

	fun argN(n1: Long, n2: Long = 0): Msg {
		this.n1 = n1
		this.n2 = n2
		return this
	}

	fun argS(s1: String, s2: String = ""): Msg {
		this.s1 = s1
		this.s2 = s2
		return this
	}

	fun addResult(value: Any): Msg {
		this.result.add(value)
		return this
	}

	fun ret(value: Any): Msg {
		this.result.add(value)
		return this
	}

	fun fire() {
		MsgCenter.fire(this)
	}

	fun fireCurrent(): ArrayList<Any> {
		return MsgCenter.fireCurrent(this)
	}

	fun fireMerge(delay: Long = 200) {
		MsgCenter.fireMerge(this, delay)
	}
}

fun fireMsg(msg: String) {
	Msg(msg).fire()
}

fun fireMsg(cls: KClass<*>) {
	Msg(cls).fire()
}

interface MsgListener {
	fun onMsg(msg: Msg)
}

@Keep
object MsgCenter {
	private val allList = ArrayList<WeakReference<MsgListener>>()

	@Synchronized
	fun listenAll(listener: MsgListener) {
		for (wl in allList) {
			if (wl.get() == listener) {
				return
			}
		}
		allList.add(WeakReference(listener))
	}


	@Synchronized
	fun remove(listener: MsgListener) {
		allList.removeAll { it.get() == null || it.get() == listener }
	}


	fun fireCurrent(msg: Msg): ArrayList<Any> {
		val ls2 = ArrayList<MsgListener>()
		sync(this) {
			val ls = allList.filter { it.get() != null }.map { it.get() }
			allList.retainAll { it.get() != null }
			ls.filterNotNullTo(ls2)
		}

		ls2.forEach {
			it.onMsg(msg)
		}
		return msg.result
	}

	fun fire(msg: Msg) {
		Task.fore {
			fireCurrent(msg)
		}
	}

	fun fire(msg: String) {
		fire(Msg(msg))
	}

	fun fire(cls: Class<*>) {
		fire(Msg(cls))
	}

	fun fire(cls: KClass<*>) {
		fire(Msg(cls.java))
	}

	fun fireMerge(msg: Msg, delay: Long = 200) {
		mergeAction("MsgCenter.mergeAction" + msg.msg, delay) {
			fire(msg)
		}
	}

	fun fireMerge(msg: String, delay: Long = 200) {
		fireMerge(Msg(msg), delay)
	}
}