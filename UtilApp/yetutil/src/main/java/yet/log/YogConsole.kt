package yet.log

/**
 * Created by entaoyang@163.com on 2018/11/8.
 */

class YogConsole : YogPrinter {

	override fun printLine(level: LogLevel, msg: String) {
		val s = Yog.formatMsg(level, msg)
		println(s)
	}

	override fun flush() {

	}

}