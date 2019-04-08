package yet.util

import android.os.Handler
import android.os.Looper
import dev.entao.yapp.App
import dev.entao.yog.Yog
import dev.entao.yog.loge
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

/**
 * Created by yet on 2015-11-20.
 */


@Suppress("UNUSED_PARAMETER")
private fun uncaughtException(thread: Thread, ex: Throwable) {
    ex.printStackTrace()
    loge(ex)
    Yog.flush()
    System.exit(-1)
}


object Task {
    val mainHandler = Handler(Looper.getMainLooper())
    val es: ScheduledExecutorService = Executors.newScheduledThreadPool(1) { r ->
        val t = Thread(r)
        t.isDaemon = true
        t.priority = Thread.NORM_PRIORITY - 1
        t.setUncaughtExceptionHandler(::uncaughtException)
        t
    }


    fun mainThread(block: BlockUnit) {
        if (InMainThread) {
            block()
        } else {
            fore(block)
        }
    }

    fun fore(callback: BlockUnit) {
        Task.mainHandler.post(callback)
    }

    fun foreDelay(delay: Long, callback: BlockUnit) {
        Task.mainHandler.postDelayed(callback, delay)
    }

    fun back(callback: () -> Unit) {
        Task.es.submit(callback)
    }

    fun backDelay(delay: Long, callback: BlockUnit) {
        Task.foreDelay(delay) {
            Task.back(callback)
        }
    }

    //返回true, 继续;  返回false, 中止
    fun backRepeat(delay: Long, callback: () -> Boolean) {
        Task.backDelay(delay) {
            val b = callback()
            if (b) {
                backRepeat(delay, callback)
            }
        }
    }

    //返回true, 继续;  返回false, 中止
    fun foreRepeat(delay: Long, callback: () -> Boolean) {
        Task.foreDelay(delay) {
            val b = callback()
            if (b) {
                foreRepeat(delay, callback)
            }
        }
    }

    private val onceProcessSet = HashSet<String>()

    //进程内只执行一次
    fun onceProcess(key: String, block: BlockUnit) {
        sync(onceProcessSet) {
            if (key in onceProcessSet) {
                return
            }
            onceProcessSet.add(key)
        }
        block.invoke()
    }

    //每个版本只运行一次
    fun onceVersion(key: String, block: BlockUnit) {
        val p = Prefer("once_${App.versionCode}")
        val exist = p.setIfNotPresent(key, true)
        if (!exist) {
            block()
        }
    }

    fun isVersionFirst(key: String): Boolean {
        val p = Prefer("first_${App.versionCode}")
        val exist = p.setIfNotPresent(key, true)
        return !exist
    }

    //maxSec秒倒计时    10, 9, 8, ... 0
//callback(second:Int), 返回false停止计时, 返回true继续计时,直到0
    fun countDown(maxSec: Int, callback: (Int) -> Boolean) {
        if (maxSec < 0) {
            return
        }
        Task.fore {
            val b = callback(maxSec)
            if (b) {
                Task.foreDelay(1000) {
                    countDown(maxSec - 1, callback)
                }
            }
        }
    }

}


val InMainThread: Boolean get() = Thread.currentThread().id == Looper.getMainLooper().thread.id


inline fun <R> sync(lock: Any, block: () -> R): R {
    return synchronized(lock, block)
}


