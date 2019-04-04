package yet.log

import yet.ext.closeSafe
import yet.util.MyDate
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

class YogDir(val logdir: File, val keepDays: Int = 30) : YogPrinter {


	private var writer: BufferedWriter? = null
	private var dateStr: String = ""

	init {
		if (!logdir.exists()) {
			logdir.mkdirs()
		}
	}

	val out: BufferedWriter?
		get() {
			val ds = MyDate().formatDate()
			if (ds == dateStr) {
				return writer
			} else {
				writer?.flush()
				writer?.closeSafe()
				writer = null
			}
			dateStr = ds
			deleteOldLogs(logdir, keepDays)
			try {
				writer = BufferedWriter(FileWriter(File(logdir, "y$dateStr.log"), true), 20 * 1024)
			} catch (ex: IOException) {
				ex.printStackTrace()
			}
			return writer
		}


	private fun deleteOldLogs(logdir: File, days: Int) {
		var n = days
		if (n < 1) {
			n = 1
		}
		val d = MyDate()
		d.addDay(-n)
		val firstKeep = "y" + d.formatDate() + ".log"
		val ls = logdir.listFiles { _, name ->
			if (name?.matches("y\\d{4}-\\d{2}-\\d{2}\\.log".toRegex()) == true) {
				name < firstKeep
			} else {
				false
			}
		}
		ls?.forEach {
			it.delete()
		}
	}

	override fun flush() {
		try {
			out?.flush()
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	override fun printLine(level: LogLevel, msg: String) {
		val w = out ?: return
		val s = Yog.formatMsg(level, msg)
		try {
			w.write(s)
			w.write("\n")
		} catch (e: IOException) {
			e.printStackTrace()
		}
		flush()

	}

}