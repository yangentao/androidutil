package dev.entao.log

import android.util.Log

/**
 * Created by entaoyang@163.com on 2016-10-28.
 */

object LogcatPrinter : YogPrinter {
    var tagName = "ylog"

    override fun printLine(level: LogLevel, msg: String) {
        var n = level.n
        if (n < Log.VERBOSE) {
            n = Log.VERBOSE
        }
        Log.println(n, tagName, msg)
    }

    override fun flush() {

    }


}