package yet.util

import java.util.*

/**
 * Created by entaoyang@163.com on 2016-07-27.
 */

object Merge {
	private val map = Hashtable<String, () -> Unit>()

	fun cancel(key: String) {
		map.remove(key)
	}

	fun fore(key: String, delayMillSeconds: Long = 300L, callback: () -> Unit) {
		if (delayMillSeconds <= 0) {
			map.remove(key)
			Task.fore {
				callback.invoke()
			}
		} else {
			map.put(key, callback)
			Task.foreDelay(delayMillSeconds) {
				val c = map.remove(key)
				c?.invoke()
			}
		}
	}

	fun back(key: String, delayMillSeconds: Long = 300L, callback: () -> Unit) {
		if (delayMillSeconds <= 0) {
			map.remove(key)
			Task.back {
				callback.invoke()
			}
		} else {
			map.put(key, callback)
			Task.backDelay(delayMillSeconds) {
				val c = map.remove(key)
				c?.invoke()
			}
		}
	}
}

fun mergeAction(key: String, ms: Long = 300, block: () -> Unit) {
	Merge.fore(key, ms, block)
}
