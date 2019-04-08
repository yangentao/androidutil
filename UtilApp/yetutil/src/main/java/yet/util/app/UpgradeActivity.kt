package yet.util.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import dev.entao.yapp.App
import dev.entao.yog.loge
import yet.ext.keepDot
import yet.ui.activities.TitledActivity
import yet.ui.ext.*
import yet.ui.viewcreator.buttonGreenRound
import yet.ui.viewcreator.textView
import yet.util.Progress
import yet.util.Task
import java.io.File


class UpgradeActivity : TitledActivity(), Progress {

    lateinit var msgView: TextView
    lateinit var upButton: Button
    lateinit var webButton: Button
    var ver: YetVersion? = null
    var apkFile: File? = null

    override fun onCreateContent(contentView: LinearLayout) {
        titleBar.title("升级")
        titleBar.rightText("检查").onClick = {
            queryVersion()
        }
        titleBar.showBack().onClick = {
            finish()
        }

        msgView = contentView.textView(LParam.WidthFill.height(80).margins(20)) {
            textSizeSp(20)
            gravityCenter()
            textColorMajor()
            text = "正在检查是否有新版本..."
        }
        upButton = contentView.buttonGreenRound {
            text = "升级"
            gone()
            onClick {
                clickUp()
            }
        }

        webButton = contentView.buttonGreenRound {
            text = "用浏览器打开"
            gone()
            onClick {
                openWeb()
            }
        }
        queryVersion()
    }

    private fun openWeb() {
        val url = ver?.downloadUrl ?: return
        App.openUrl(url)
    }

    private fun sizeText(n: Int): String {
        return when {
            n > 1000_000 -> (n.toDouble() / 1000_000).keepDot(2) + "M"
            n > 1000 -> (n.toDouble() / 1000).keepDot(2) + "K"
            else -> "$n 字节"
        }

    }

    override fun onProgressStart(total: Int) {
        Task.fore {
            msgView.text = "正在下载:共${sizeText(total)}"
        }
    }

    override fun onProgress(current: Int, total: Int, percent: Int) {
        Task.fore {
            msgView.text = "正在下载:共${sizeText(total)}, $percent%"
        }
    }

    override fun onProgressFinish() {
        Task.fore {
            msgView.text = "下载完成"
        }
    }

    private fun download() {
        val oldFile = apkFile
        if (oldFile != null) {
            Task.fore {
                install(oldFile)
            }
            return
        }

        val v = ver ?: return
        Task.back {
            val f = YetVersion.download(v.resId, this@UpgradeActivity)
            Task.fore {
                if (f != null) {
                    apkFile = f
                    install(f)
                }
            }
        }
    }

    private fun install(file: File) {
        val apkUri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            FileProv.uriOfFile(file)
        }
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        val ls = packageManager.queryIntentActivities(intent, 0)
        if (ls.isEmpty()) {
            msgView.text = "未找到APK安装程序"
            return
        }
        try {
            startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
            loge(ex)
        }
    }

    private fun clickUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val hasInstallPermission = packageManager.canRequestPackageInstalls()
            if (!hasInstallPermission) {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName"))
                startActivityForResult(intent, 1)
                return
            }
        }
        download()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (packageManager.canRequestPackageInstalls()) {
                    download()
                } else {
                    msgView.text = "没有得到安装apk文件的授权"
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun queryVersion() {
        Task.back {
            val v = YetVersion.check()
            Task.fore {
                if (v == null || !v.great()) {
                    msgView.text = "已经是最新版本"
                } else {
                    val m = if (v.msg != v.versionName) {
                        v.msg
                    } else {
                        ""
                    }
                    msgView.text = "发现新版本: ${v.versionName} $m"
                    upButton.visiable()
                    webButton.visiable()
                    ver = v
                }
            }
        }
    }
}