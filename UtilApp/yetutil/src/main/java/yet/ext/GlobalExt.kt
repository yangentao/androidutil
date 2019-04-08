package yet.ext

import dev.entao.ybase.closeSafe
import java.io.Closeable
import java.text.Collator
import java.util.*

/**
 * Created by entaoyang@163.com on 16/7/20.
 */


val collatorChina = Collator.getInstance(Locale.CHINA)
val chinaComparator = Comparator<String> { left, right -> collatorChina.compare(left, right) }

class ChinaComparator<T>(val block: (T) -> String) : Comparator<T> {
	override fun compare(o1: T, o2: T): Int {
		return chinaComparator.compare(block(o1), block(o2))
	}
}


inline fun <T : Closeable> T?.useSafe(block: (T) -> Unit) {
	try {
		if (this != null) {
			block(this)
		}
	} catch (ex: Exception) {
		ex.printStackTrace()
	} finally {
		this.closeSafe()
	}
}
