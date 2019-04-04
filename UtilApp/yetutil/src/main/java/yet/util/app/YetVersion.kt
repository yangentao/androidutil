package yet.util.app

import android.app.Activity
import yet.net.Http
import yet.sql.MapTable
import yet.ui.dialogs.DialogX
import yet.ui.ext.openActivity
import yet.ui.page.BaseFragment
import yet.util.MyFiles
import yet.util.Progress
import yet.util.Task
import yet.util.ToastUtil
import yet.yson.YsonObject
import java.io.File

/**
 * Created by entaoyang@163.com on 2017-06-13.
 */

class YetVersion(val jo: YsonObject) {
    val versionCode: Int by jo
    val versionName: String by jo
    val msg: String by jo
    val resId: Int by jo


    fun great(): Boolean {
        return versionCode > App.versionCode
    }

    val downloadUrl: String
        get() {
            return Http(YetVersion.SERVER_DOWN).arg("id", resId).buildGetUrl()
        }

    companion object {
        var SERVER = "http://app800.cn"
        val SERVER_CHECK: String get() = SERVER.trimEnd('/') + "/apps/check"
        val SERVER_DOWN: String get() = SERVER.trimEnd('/') + "/apps/res/download"
        var CHECK_HOURS = 4 //最多每4小时检查一次

        private val ignoreMap = MapTable("ver_ignore")

        private var lastCheckUpdate: Long by MapTable.config

        private fun isIgnored(verCode: Int): Boolean {
            return ignoreMap.get(verCode.toString()) != null
        }

        fun checkByUser(page: BaseFragment) {
            page.openActivity(UpgradeActivity::class)
        }

        fun checkAuto(page: BaseFragment) {
            val last = lastCheckUpdate
            val now = System.currentTimeMillis()
            if (now - last < CHECK_HOURS * 60 * 60 * 1000) {
                return
            }
            Task.back {
                val v = check()
                Task.fore {
                    if (v != null) {
                        lastCheckUpdate = now
                        if (!isIgnored(v.versionCode)) {
                            if (page.activity != null) {
                                confirmInstall(page.activity, v, true)
                            }
                        }
                    }

                }
            }
        }

        private fun confirmInstall(act: Activity, v: YetVersion, quiet: Boolean) {
            if (!v.great()) {
                try {
                    if (!quiet) {
                        ToastUtil.showLong(act, "已经是最新版本")
                    }
                } catch (ex: Exception) {
                }
                return
            }

            DialogX(act).apply {
                title("检查升级")
                bodyText("发现新版本${v.versionName}")
                buttons {
                    ok("升级") {
                        act.openActivity(UpgradeActivity::class)
                    }
                    normal("忽略此版本") {
                        ignoreMap.put(v.versionCode.toString(), v.versionName)
                    }
                    cancel("取消")
                }
                show()
            }
        }


        fun check(): YetVersion? {
            val r = Http(SERVER_CHECK).arg("pkg", App.packageName).get()
            if (r.OK) {
                val jo = r.ysonObject() ?: return null
                if (jo.int("code") != 0) {
                    return null
                }
                val jdata = jo.obj("data") ?: return null
                return YetVersion(jdata)
            }
            return null
        }

        fun download(resId: Int, progress: Progress?): File? {
            val url = Http(YetVersion.SERVER_DOWN).arg("id", resId).buildGetUrl()
            val f = MyFiles.ex.temp(App.app_name + ".apk")
            val r = Http(url).download(f, progress)
            if (r.OK) {
                return f
            }
            return null
        }

    }
}