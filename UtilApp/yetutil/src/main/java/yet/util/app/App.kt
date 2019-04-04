@file:Suppress("DEPRECATION")

package yet.util.app

import android.app.*
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.StrictMode
import android.telephony.TelephonyManager
import android.text.ClipboardManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import yet.log.Yog
import yet.log.logd
import yet.log.loge
import yet.theme.TextSize
import yet.ui.activities.AnimConf
import yet.ui.ext.displayMetrics
import yet.util.MsgCenter
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.lang.ref.WeakReference

/**
 * Created by entaoyang@163.com on 2017-03-31.
 */

object App {
    val NETWORK_CONNECTED = "network.connected"
    private lateinit var inst: WeakReference<Application>

    val app: Application get() = inst.get()!!

    val debug: Boolean by lazy { 0 != (app.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) }
    val ic_launcher: Int by lazy { app.applicationInfo.icon }
    val density: Float by lazy { app.resources.displayMetrics.density }
    val scaledDensity: Float by lazy { app.resources.displayMetrics.scaledDensity }
    val app_name: String by lazy { app.applicationInfo.loadLabel(app.packageManager).toString() }
    val packageName: String by lazy { app.packageName }
    val versionCode: Int by lazy { app.packageManager.getPackageInfo(app.packageName, 0).versionCode }
    val versionName: String by lazy { app.packageManager.getPackageInfo(app.packageName, 0).versionName }
    val sdkVersion: Int by lazy { Build.VERSION.SDK_INT }
    val modelBuild: String by lazy { Build.MODEL }

    val resolver: ContentResolver by lazy { app.contentResolver }
    val contentResolver: ContentResolver  by lazy { app.contentResolver }
    val resource: Resources by lazy { app.resources }

    var animConfDefault = AnimConf.RightIn

    val screenWidthPx: Int by lazy {
        app.displayMetrics.widthPixels
    }
    val screenHeightPx: Int by lazy {
        app.displayMetrics.heightPixels
    }

    fun setInstance(inst: Application) {
        this.inst = WeakReference(inst)
        val builder = StrictMode.VmPolicy.Builder()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure().penaltyLog()
        }
        StrictMode.setVmPolicy(builder.build())
        if (OS.HUAWEI) {
            TextSize.addTextSize(-1)
        }

        Thread.setDefaultUncaughtExceptionHandler { _, ex ->
            ex.printStackTrace()
            loge(ex)
            Yog.flush()
            System.exit(-1)
        }


        val connectivityReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                    MsgCenter.fire(ConnectivityManager::class)
                }
                if (isNetworkConnected) {
                    MsgCenter.fire(NETWORK_CONNECTED)
                }

            }
        }
        app.registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


    }

    fun openOrCreateDatabase(name: String): SQLiteDatabase {
        return app.openOrCreateDatabase(name, 0, null)
    }

    val sdAppRoot: File
        get() {
            return app.getExternalFilesDir(null)
        }


    fun sp2px(spValue: Float): Int {
        return (spValue * scaledDensity + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Float): Int {
        if (dpValue > 0) {
            return (dpValue * density + 0.5f).toInt()
        } else {
            return dpValue.toInt()
        }
    }

    fun dp2px(dpValue: Int): Int {
        if (dpValue > 0) {
            return (dpValue * density + 0.5f).toInt()
        } else {
            return dpValue
        }
    }

    @Suppress("DEPRECATION")
    fun isForeground(): Boolean {
        val am: ActivityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            ?: return false
        val cn = am.getRunningTasks(1)[0].topActivity
        val currentPackageName = cn.packageName
        return !TextUtils.isEmpty(currentPackageName) && currentPackageName == packageName
    }

    fun isAppShowing(): Boolean {
        return isForeground() && !keyguardManager.inKeyguardRestrictedInputMode()
    }

    fun isAppTop(): Boolean {
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            return false
        }
        val am: ActivityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
            ?: return false
        for (info in am.runningAppProcesses) {

            if (info.processName == packageName) {
                logd(info.processName, info.importance)
                return info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }


    @Throws(FileNotFoundException::class)
    fun openStream(uri: Uri): InputStream = app.contentResolver.openInputStream(uri)


    // 获取ApiKey
    fun getMetaValue(context: Context, metaKey: String): String? {
        try {
            val ai = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
            return ai?.metaData?.getString(metaKey)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            loge(e)
        }

        return null
    }

    fun metaValue(metaKey: String): String? {
        return getMetaValue(app, metaKey)
    }


    // 单位兆M
    val memLimit: Int by lazy {
        (app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass
    }

    val isNetworkConnected: Boolean
        get() {
            return (app.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected
                ?: false
        }


    fun systemService(name: String): Any? {
        return app.getSystemService(name)
    }

    val downloadManager: DownloadManager
        get() {
            return app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }

    val telephonyManager: TelephonyManager
        get() {
            return app.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        }


    val powerManager: PowerManager
        get() {
            return app.getSystemService(Context.POWER_SERVICE) as PowerManager
        }

    val getKeyguardManager: KeyguardManager
        get() {
            return app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        }

    val connectivityManager: ConnectivityManager
        get() {
            return app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }

    val keyguardManager: KeyguardManager
        get() {
            return app.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        }

    val notificationManager: NotificationManager
        get () {
            return app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

    @Suppress("DEPRECATION")
    val clipText: String
        get() {
            val cm = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            return cm.text.toString()
        }


    fun showInputMethod(view: View) {
        val imm = app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    @Suppress("DEPRECATION")
    fun copyToClipboard(text: String) {
        val clip = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clip.text = text
    }

    fun installShortcut(name: String, imageRes: Int, cls: Class<*>, exKey: String, exValue: String) {
        val it = Intent(Intent.ACTION_MAIN)
        it.addCategory(Intent.CATEGORY_LAUNCHER)
        it.setClass(App.app, cls)
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        if (exKey.isNotEmpty()) {
            it.putExtra(exKey, exValue)
        }
        installShortcut(name, imageRes, it)
    }

    @Suppress("DEPRECATION")
    private fun installShortcut(name: String, imageRes: Int, intent: Intent) {
        val ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT"
        val addShortcutIntent = Intent(ACTION_INSTALL_SHORTCUT)
        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false)// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name)
        addShortcutIntent.putExtra(
            Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
            Intent.ShortcutIconResource.fromContext(App.app, imageRes)
        )
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent)
        app.sendBroadcast(addShortcutIntent)
    }

    fun installApkFile(file: File) {
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
        try {
            app.startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
            loge(ex)
        }
    }

    fun installApk(apkUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        try {
            app.startActivity(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
            loge(ex)
        }
    }

    fun openUrl(url: String) {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.data = Uri.parse(url)
            app.startActivity(i)
        } catch (ex: Exception) {
            loge(ex)
        }
    }


}
