package yet.log

import yet.util.MyFiles
import yet.util.MyDate
import yet.util.log.LogcatPrinter
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

/**
 * Created by entaoyang@163.com on 2018/11/8.
 */

object Yog {
    var printer: YogPrinter? = YogTree(LogcatPrinter(), YogDir(MyFiles.ex.logDir, 10))
    var level = LogLevel.DEBUG

    fun flush() {
        printer?.flush()
    }

    fun d(vararg args: Any?) {
        printMessage(LogLevel.DEBUG, *args)
    }

    fun w(vararg args: Any?) {
        printMessage(LogLevel.WARN, *args)
    }

    fun e(vararg args: Any?) {
        printMessage(LogLevel.ERROR, *args)
        printer?.flush()
    }

    fun i(vararg args: Any?) {
        printMessage(LogLevel.INFO, *args)
    }


    fun fatal(vararg args: Any?) {
        e(*args)
        throw RuntimeException("fatal error!")
    }

    fun formatMsg(level: LogLevel, msg: String): String {
        val sb = StringBuilder(msg.length + 64)
        val date = MyDate().formatDateTimeX()
        sb.append(date)
        sb.append(String.format(Locale.getDefault(), "%6d ", Thread.currentThread().id))
        sb.append(level.name)
        sb.append(" ")
        sb.append(msg)
        return sb.toString()
    }

    fun printMessage(level: LogLevel, vararg args: Any?) {
        if (this.level != LogLevel.DISABLE && level.ge(this.level)) {
            val s: String = args.joinToString(" ") {
                toLogString(it)
            }
            printer?.printLine(level, s)
        }
    }

    fun toLogString(obj: Any?): String {
        if (obj == null) {
            return "null"
        }
        if (obj is String) {
            return obj
        }
        if (obj.javaClass.isPrimitive) {
            return obj.toString()
        }

        if (obj is Throwable) {
            val sw = StringWriter(512)
            val pw = PrintWriter(sw)
            obj.printStackTrace(pw)
            return sw.toString()
        }

        if (obj is Array<*>) {
            val s = obj.joinToString(",") { toLogString(it) }
            return "ARRAY[$s]"
        }
        if (obj is List<*>) {
            val s = obj.joinToString(", ") { toLogString(it) }
            return "LIST[$s]"
        }
        if (obj is Map<*, *>) {
            val s = obj.map { "${toLogString(it.key)} = ${toLogString(it.value)}" }.joinToString(",")
            return "MAP{$s}"
        }
        if (obj is Iterable<*>) {
            val s = obj.joinToString(", ") { toLogString(it) }
            return "ITERABLE[$s]"
        }
        return obj.toString()
    }
}