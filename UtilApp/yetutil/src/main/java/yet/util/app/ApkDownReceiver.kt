package yet.util.app

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import dev.entao.sql.MapTable
import dev.entao.yapp.App
import dev.entao.yog.logd

class ApkDownReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        logd("下载完成 Receiver ID: ", id)
        if (id == -1L) {
            return
        }
        downMap.getString(id.toString()) ?: return
        val fileUri = App.downloadManager.getUriForDownloadedFile(id) ?: return
        logd(fileUri)
        App.installApk(fileUri)
    }


    companion object {
        val downMap = MapTable("downloadTasks")

        fun downloadAndInstall(url: String, title: String, desc: String = "") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                App.openUrl(url)
                return
            }


            val req = DownloadManager.Request(Uri.parse(url))
            if (title.isNotEmpty()) {
                req.setTitle(title)
            }
            if (desc.isNotEmpty()) {
                req.setDescription(desc)
            }
            req.setVisibleInDownloadsUi(true)
            req.setAllowedOverRoaming(true)
            req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${App.appName}.apk")


            val downId = App.downloadManager.enqueue(req)
            logd("DownloadTask enqueue: ", downId)
            downMap.put(downId.toString(), url)

        }

    }
}
