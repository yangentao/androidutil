package yet.util.log

import android.util.Log
import yet.log.LogLevel
import yet.log.YogPrinter

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

class LogcatPrinter : YogPrinter {
	override fun printLine(level: LogLevel, msg: String) {
		var n = level.n
		if (n < Log.VERBOSE) {
			n = Log.VERBOSE
		}
		Log.println(n, tagName, msg)
	}

	override fun flush() {

	}

	companion object {
		val tagName = "ylog"
	}
}