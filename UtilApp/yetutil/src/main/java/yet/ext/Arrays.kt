package yet.ext

/**
 * Created by entaoyang@163.com on 16/5/12.
 */

fun equal(a1: ByteArray, a2: ByteArray): Boolean {
	if (a1.size != a2.size) {
		return false
	}
	for (i in a1.indices) {
		if (a1[i] != a2[i]) {
			return false
		}
	}
	return true
}

inline fun <reified T> toArray(ls: List<T>): Array<T> {
	return Array<T>(ls.size) { ls[it] }
}