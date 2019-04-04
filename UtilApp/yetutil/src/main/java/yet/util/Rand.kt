package yet.util

import java.util.*

/**
 * Created by entaoyang@163.com on 2018-07-30.
 */

object Rand {
	val random = Random(System.currentTimeMillis())

	fun int(bound: Int): Int {
		return random.nextInt(bound)
	}

	fun code4(): String {
		val n = random.nextInt(8999)
		val m = n + 1000
		return m.toString()
	}

	fun code6(): String {
		val n = random.nextInt(899999)
		val m = n + 100000
		return m.toString()
	}
}