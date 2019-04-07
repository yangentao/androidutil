package dev.entao.yog

/**
 * Created by entaoyang@163.com on 2018/11/8.
 */

interface YogPrinter {
    fun flush()
    fun printLine(level: LogLevel, msg: String)
    fun uninstall() {}
}