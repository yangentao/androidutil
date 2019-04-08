package yet.ui.page

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Fragment
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.SparseArray
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import dev.entao.yapp.App
import dev.entao.yog.Yog
import dev.entao.yog.loge
import yet.ext.getValue
import yet.theme.Str
import yet.ui.activities.AnimConf
import yet.ui.activities.Pages
import yet.ui.activities.TabBarActivity
import yet.ui.dialogs.DialogX
import yet.ui.dialogs.GridConfig
import yet.ui.dialogs.HorProgressDlg
import yet.ui.dialogs.SpinProgressDlg
import yet.ui.res.Bmp
import yet.ui.res.saveJpg
import yet.ui.res.savePng
import yet.ui.widget.TabBar
import yet.util.*
import yet.util.app.Perm
import java.io.File
import kotlin.reflect.KProperty1


/**
 * Created by entaoyang@163.com on 16/3/12.
 */


/**
 * 不要调用getActivity().finish(). 要调用finish(), finish处理了动画
 * fragment基类 公用方法在此处理
 */
open class BaseFragment : Fragment(), MsgListener {
    private val resultListeners = SparseArray<PreferenceManager.OnActivityResultListener>(8)
    lateinit var spinProgressDlg: SpinProgressDlg
    lateinit var horProgressDlg: HorProgressDlg

    var fullScreen = false
    var windowBackColor: Int? = null

    var openFlag: Int = 0

    var activityAnim: AnimConf? = AnimConf.RightIn


    val watchMap = HashMap<Uri, ContentObserver>()

    fun openWeb(title: String, url: String) {
        WebPage.open(activity, title, url)
    }

    fun openAssetHtml(title: String, file: String) {
        WebPage.openAsset(activity, title, file)
    }

    fun smsTo(phoneSet: Set<String>, body: String = "") {
        if (phoneSet.isNotEmpty()) {
            smsTo(phoneSet.joinToString(";"), body)
        }
    }

    fun smsTo(phone: String, body: String = "") {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone))
        if (body.isNotEmpty()) {
            intent.putExtra("sms_body", body)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }


    fun dial(phone: String) {
        try {
            val uri = Uri.fromParts("tel", phone, null)
            val it = Intent(Intent.ACTION_DIAL, uri)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(it)
        } catch (e: Throwable) {
            loge(e)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Perm.onPermResult(requestCode)
    }


    fun singleTop() {
        openFlag = openFlag or Intent.FLAG_ACTIVITY_SINGLE_TOP
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        spinProgressDlg = SpinProgressDlg(activity)
        horProgressDlg = HorProgressDlg(activity)
        MsgCenter.listenAll(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun statusBarColor(color: Int) {
        val w = activity?.window ?: return
        if (Build.VERSION.SDK_INT >= 21) {
            w.statusBarColor = color
        }
    }

    fun <T : Any> selectItemT(title: String, items: Collection<T>, prop: KProperty1<*, *>, resultBlock: (T) -> Unit) {
        selectItemT(title, items, { prop.getValue(it)?.toString() ?: "" }, resultBlock)
    }

    fun selectItem(items: Collection<Any>, prop: KProperty1<*, *>, resultBlock: (Any) -> Unit) {
        selectItem(items, { prop.getValue(it)?.toString() ?: "" }, resultBlock)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> selectItemT(
        title: String,
        items: Collection<T>,
        displayBlock: (T) -> String,
        resultBlock: (T) -> Unit
    ) {
        DialogX.listItem(activity, items.toList(), title, { displayBlock(it as T) }, { resultBlock(it as T) })
    }

    fun selectItem(items: Collection<Any>, displayBlock: (Any) -> String, resultBlock: (Any) -> Unit) {
        DialogX.listItem(activity, items.toList(), "", displayBlock, resultBlock)
    }

    fun selectString(items: Collection<String>, resultBlock: (String) -> Unit) {
        DialogX.listItem(activity, items.toList(), "", { it as String }) {
            resultBlock(it as String)
        }
    }

    fun selectStringN(items: Collection<String>, block: (Int) -> Unit) {
        DialogX.listStringN(activity, items.toList(), "", block)
    }


    fun selectGrid(items: List<Any>, callback: GridConfig.() -> Unit) {
        DialogX.selectGrid(activity, items, callback)
    }

    fun showDialog(block: DialogX.() -> Unit): DialogX {
        val d = DialogX(activity)
        d.block()
        d.show()
        return d
    }

    override fun onResume() {
        super.onResume()
        if (!isHidden) {
            onShow()
        }
    }

    override fun onPause() {
        if (!isHidden) {
            onHide()
        }
        Yog.flush()
        super.onPause()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            if (isResumed) {
                onHide()
            }
        } else {
            if (isResumed) {
                onShow()
            }
        }
    }

    open fun onShow() {

    }

    open fun onHide() {

    }

    /**
     * 可见, 并且没锁屏

     * @return
     */
    val isVisiableToUser: Boolean
        get() = this.isResumed && isVisible && !App.keyguardManager.inKeyguardRestrictedInputMode()

    fun takeViedo(sizeM: Int, block: (Uri) -> Unit) {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, sizeM * 1024 * 1024)
        val onResult = PreferenceManager.OnActivityResultListener { _, resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    block.invoke(data.data)
                }
            }
            true
        }
        startActivityForResult(TAKE_VIDEO, intent, onResult)
    }

    fun pickVideo(block: (Uri) -> Unit) {
        val i = Intent(Intent.ACTION_PICK)
        i.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*")
        val onResult = PreferenceManager.OnActivityResultListener { _, resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    block.invoke(data.data)
                }
            }
            true
        }
        startActivityForResult(PICK_PHOTO, i, onResult)
    }

    fun selectImage(width: Int, block: (Uri) -> Unit) {
        selectString(listOf("拍照", "相册")) {
            if (it == "拍照") {
                takePhotoJpg(width) {
                    block(Uri.fromFile(it))
                }
            } else {
                pickPhoto(width) {
                    block(it)
                }
            }
        }
    }

    fun pickJpg(width: Int, block: (File) -> Unit) {
        val i = Intent(Intent.ACTION_PICK)
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        val onResult = PreferenceManager.OnActivityResultListener { _, resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    val outputFile = MyFiles.ex.temp("" + System.currentTimeMillis() + ".jpg")
                    val bmp = Bmp.uri(data.data, width, Bitmap.Config.ARGB_8888)
                    if (bmp != null) {
                        bmp.saveJpg(outputFile)
                        block.invoke(outputFile)
                    }
                }
            }
            true
        }
        startActivityForResult(PICK_PHOTO, i, onResult)
    }

    fun pickPhoto(width: Int, block: (Uri) -> Unit) {
        val i = Intent(Intent.ACTION_PICK)
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        val onResult = PreferenceManager.OnActivityResultListener { _, resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.data != null) {
                    val f = MyFiles.ex.tempFile("PNG")
                    val bmp = Bmp.uri(data.data, width, Bitmap.Config.ARGB_8888)
                    if (bmp != null) {
                        bmp.savePng(f)
                        if (f.exists()) {
                            block.invoke(Uri.fromFile(f))
                        }
                    }

                }
            }
            true
        }
        startActivityForResult(PICK_PHOTO, i, onResult)
    }

    fun takePhotoPng(width: Int, block: (File) -> Unit) {
        takePhoto(width, true, block)
    }

    fun takePhotoJpg(width: Int, block: (File) -> Unit) {
        takePhoto(width, false, block)
    }

    fun takePhoto(width: Int, png: Boolean, block: (File) -> Unit) {
        val FMT = if (png) "PNG" else "JPEG"
        val outputFile = MyFiles.ex.temp("" + System.currentTimeMillis() + "." + FMT)
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
        var outUri = UriFromSdFile(outputFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
        intent.putExtra("outputFormat", FMT)
        val onResult = PreferenceManager.OnActivityResultListener { _, resultCode, _ ->
            if (resultCode == Activity.RESULT_OK && outputFile.exists()) {
                val f = MyFiles.ex.tempFile(FMT.toLowerCase())
                val bmp = Bmp.file(outputFile, width, Bitmap.Config.ARGB_8888)
                if (bmp != null) {
                    if (png) {
                        bmp.savePng(f)
                    } else {
                        bmp.saveJpg(f)
                    }
                    if (f.exists()) {
                        block(f)
                    }
                }
            }
            true
        }
        startActivityForResult(TAKE_PHOTO, intent, onResult)
    }


    fun cropPhoto(uri: Uri, outX: Int, outY: Int, result: (Bitmap?) -> Unit) {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outX)
        intent.putExtra("outputY", outY)
        intent.putExtra("return-data", true)
        // intent.putExtra("output",CAMERA_EXTRA_OUTPUT_FILE);
        val onResult = PreferenceManager.OnActivityResultListener { _, resultCode, data ->
            if (resultCode == Activity.RESULT_OK) {
                val extras = data.extras
                var photo: Bitmap? = null
                if (extras != null) {
                    photo = extras.getParcelable<Bitmap>("data")
                }
                result.invoke(photo)
            } else {
                result.invoke(null)
            }
            true
        }
        startActivityForResult(CROP_PHOTO, intent, onResult)
    }

    fun startActivityForResult(requestCode: Int, intent: Intent, onResult: PreferenceManager.OnActivityResultListener) {
        resultListeners.put(requestCode, onResult)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val rl = resultListeners.get(requestCode)
        if (rl != null) {
            resultListeners.remove(requestCode)
            rl.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    fun finish() {
        val context = activity
        context.finish()
    }

    val tabBar: TabBar?
        get() {
            if (activity is TabBarActivity) {
                return (activity as TabBarActivity).tabBar
            }
            return null
        }

    fun toastIf(condition: Boolean, trueString: String, falseString: String) {
        if (condition) {
            toast(trueString)
        } else {
            toast(falseString)
        }
    }

    fun toastIf(condition: Boolean, trueString: String) {
        if (condition) {
            toast(trueString)
        }
    }

    fun toast(vararg texts: Any) {
        val s = texts.map { it.toString() }.joinToString(", ")
        Task.fore {
            if (activity != null) {
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(App.inst, s, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun toastSuccessFailed(b: Boolean) {
        toast(if (b) Str.OP_SUCCESS else Str.OP_FAILED)
    }

    fun toastShort(text: String) {
        Task.fore {
            if (activity != null) {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(App.inst, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun softInputAdjustResize() {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    fun hideInputMethod() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(
                activity.getCurrentFocus()!!.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    fun showInputMethod() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // 显示或者隐藏输入法
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return false
    }


    fun unWatch(uri: Uri) {
        val ob = watchMap[uri]
        if (ob != null) {
            activity.contentResolver.unregisterContentObserver(ob)
        }
    }

    fun watch(uri: Uri, block: (Uri) -> Unit = {}) {
        if (watchMap.containsKey(uri)) {
            return
        }
        val ob = object : ContentObserver(Handler(Looper.getMainLooper())) {

            override fun onChange(selfChange: Boolean, uri: Uri) {
                mergeAction("watchUri:$uri") {
                    block(uri)
                    onUriChanged(uri)
                }
            }
        }
        watchMap[uri] = ob
        activity.contentResolver.registerContentObserver(uri, true, ob)
    }

    open fun onUriChanged(uri: Uri) {

    }


    fun pickDate(initDate: Long, block: (Long) -> Unit) {
        pickDate(MyDate(initDate), block)
    }

    fun pickDate(date: MyDate, block: (Long) -> Unit) {
        val dlg = DatePickerDialog(activity, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                block(MyDate.makeDate(year, monthOfYear, dayOfMonth))
            }

        }, date.year, date.month, date.day)
        dlg.show()
    }

    fun pickTime(time: Long, block: (Long) -> Unit) {
        pickTime(MyDate(time), block)
    }

    fun pickTime(time: MyDate, block: (Long) -> Unit) {
        val dlg = TimePickerDialog(activity, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                block(MyDate.makeTime(hourOfDay, minute))
            }

        }, time.hour, time.minute, true)
        dlg.show()
    }

    fun viewImage(uri: Uri) {
        val intent = Intent()
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = android.content.Intent.ACTION_VIEW
        intent.setDataAndType(uri, "image/*")
        startActivity(intent)
    }


    override fun onDestroy() {
        for (ob in watchMap.values) {
            activity.contentResolver.unregisterContentObserver(ob)
        }
        watchMap.clear()
        MsgCenter.remove(this)
        Pages.onDestroy(this)
        super.onDestroy()
    }

    override fun onMsg(msg: Msg) {
    }

    companion object {
        private val TAKE_PHOTO = 988
        private val TAKE_VIDEO = 989
        private val PICK_PHOTO = 977
        private val CROP_PHOTO = 966
    }

}
