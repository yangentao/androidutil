package dev.entao.yog

/**
 * Created by entaoyang@163.com on 2018/11/8.
 */

class YogTree(vararg ps: YogPrinter) : YogPrinter {
    val all = ArrayList<YogPrinter>()

    init {
        all += ps
    }

    override fun flush() {
        all.forEach { it.flush() }
    }

    override fun printLine(level: LogLevel, msg: String) {
        all.forEach { it.printLine(level, msg) }
    }

    override fun uninstall() {
        for (p in all) {
            p.uninstall()
        }
    }

}