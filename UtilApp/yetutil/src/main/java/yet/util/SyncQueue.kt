package yet.util

import java.util.*

/**
 * Created by entaoyang@163.com on 16/6/17.
 */


class SyncQueue<T> {
	val queue = LinkedList<T>()

	fun clear() {
		queue.clear()
	}

	fun push(e: T) {
		synchronized(this) {
			queue.add(e)
		}
	}

	fun pop(): T? {
		synchronized(this) {
			return queue.pollFirst()
		}
	}

	fun peek(): T? {
		synchronized(this) {
			return queue.peekFirst()
		}
	}
}