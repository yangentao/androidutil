package dev.entao.log

/**
 * Created by entaoyang@163.com on 2016/12/20.
 */

enum class LogLevel(val n: Int) {
    DISABLE(0), DEBUG(3), INFO(4), WARN(5), ERROR(6), FATAIL(7);

    //>=
    fun ge(level: LogLevel): Boolean {
        return this.ordinal >= level.ordinal
    }
}