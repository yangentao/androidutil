package yet.util

/**
 * Created by entaoyang@163.com on 2016-07-27.
 */


object Bit {
	fun has(value: Int, flag: Int): Boolean {
		return value and flag != 0
	}

	fun remove(value: Int, flag: Int): Int {
		return value and flag.inv()
	}
}

val Int.high0: Byte get() = ((this ushr 24) and 0x00ff).toByte()
val Int.high1: Byte get() = ((this ushr 16) and 0x00ff).toByte()
val Int.high2: Byte get() = ((this ushr 8) and 0x00ff).toByte()
val Int.high3: Byte get() = (this and 0x00ff).toByte()

val Int.low0: Byte get() = (this and 0x00ff).toByte()
val Int.low1: Byte get() = ((this ushr 8) and 0x00ff).toByte()
val Int.low2: Byte get() = ((this ushr 16) and 0x00ff).toByte()
val Int.low3: Byte get() = ((this ushr 24) and 0x00ff).toByte()