package dev.entao.ui.page

import android.app.Activity
import android.app.DatePickerDialog
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
import android.support.v4.app.Fragment
import android.util.SparseArray
import android.view.KeyEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import dev.entao.appbase.App
import dev.entao.appbase.ex.Bmp
import dev.entao.appbase.ex.saveJpg
import dev.entao.appbase.ex.savePng
import dev.entao.base.MyDate
import dev.entao.base.getValue
import dev.entao.log.Yog
import dev.entao.log.loge
import dev.entao.theme.Str
import dev.entao.ui.activities.TabBarActivity
import dev.entao.ui.dialogs.DialogX
import dev.entao.ui.dialogs.GridConfig
import dev.entao.ui.dialogs.HorProgressDlg
import dev.entao.ui.dialogs.SpinProgressDlg
import dev.entao.ui.ext.act
import dev.entao.ui.widget.TabBar
import dev.entao.util.*
import dev.entao.util.app.Perm
import java.io.File
import kotlin.collections.set
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

	var activityAnim: dev.entao.ui.activities.AnimConf? = dev.entao.ui.activities.AnimConf.RightIn


	val watchMap = HashMap<Uri, ContentObserver>()

	fun openWeb(title: String, url: String) {
		WebPage.open(act, title, url)
	}

	fun openAssetHtml(title: String, file: String) {
		WebPage.openAsset(act, title, file)
	}

	fun smsTo(phoneSet: Set<String>, body: String = "") {
		if (phoneSet.isNotEmpty()) {
			smsTo(phoneSet.joinToString(";"), body)
		}
	}

	fun smsTo(phone: String, body: String = "") {
		val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone"))
		if (body.isNotEmpty()) {
			intent.putExtra("sms_body", body)
		}
		intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
		act.startActivity(intent)
	}


	fun dial(phone: String) {
		try {
			val uri = Uri.fromParts("tel", phone, null)
			val it = Intent(Intent.ACTION_DIAL, uri)
			it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
			act.startActivity(it)
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
		spinProgressDlg = SpinProgressDlg(act)
		horProgressDlg = HorProgressDlg(act)
		MsgCenter.listenAll(this)
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
		DialogX.listItem(act, items.toList(), title, { displayBlock(it as T) }, { resultBlock(it as T) })
	}

	fun selectItem(items: Collection<Any>, displayBlock: (Any) -> String, resultBlock: (Any) -> Unit) {
		DialogX.listItem(act, items.toList(), "", displayBlock, resultBlock)
	}

	fun selectString(items: Collection<String>, resultBlock: (String) -> Unit) {
		DialogX.listItem(act, items.toList(), "", { it as String }) {
			resultBlock(it as String)
		}
	}

	fun selectStringN(items: Collection<String>, block: (Int) -> Unit) {
		DialogX.listStringN(act, items.toList(), "", block)
	}


	fun selectGrid(items: List<Any>, callback: GridConfig.() -> Unit) {
		DialogX.selectGrid(act, items, callback)
	}

	fun showDialog(block: DialogX.() -> Unit): DialogX {
		val d = DialogX(act)
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
				takePhotoJpg(width) { fff ->
					block(Uri.fromFile(fff))
				}
			} else {
				pickPhoto(width) { uu ->
					block(uu)
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
					val outputFile = App.files.ex.temp("" + System.currentTimeMillis() + ".jpg")
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
					val f = App.files.ex.tempFile("PNG")
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
		val outputFile = App.files.ex.temp("" + System.currentTimeMillis() + "." + FMT)
		val intent = Intent("android.media.action.IMAGE_CAPTURE")
		intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
		val outUri = UriFromSdFile(outputFile)
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
		intent.putExtra("outputFormat", FMT)
		val onResult = PreferenceManager.OnActivityResultListener { _, resultCode, _ ->
			if (resultCode == Activity.RESULT_OK && outputFile.exists()) {
				val f = App.files.ex.tempFile(FMT.toLowerCase())
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
					photo = extras.getParcelable("data")
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
		activity?.finish()
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
		val s = texts.joinToString(", ") { it.toString() }
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
		act.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
	}

	fun hideInputMethod() {
		val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		if (imm.isActive && act.currentFocus != null) {
			imm.hideSoftInputFromWindow(
				act.currentFocus!!.windowToken,
				InputMethodManager.HIDE_NOT_ALWAYS
			)
		}
	}

	fun showInputMethod() {
		val imm = act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		// 显示或者隐藏输入法
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
	}

	open fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		return false
	}


	fun unWatch(uri: Uri) {
		val ob = watchMap[uri]
		if (ob != null) {
			act.contentResolver.unregisterContentObserver(ob)
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
		act.contentResolver.registerContentObserver(uri, true, ob)
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
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
		intent.action = android.content.Intent.ACTION_VIEW
		intent.setDataAndType(uri, "image/*")
		startActivity(intent)
	}


	override fun onDestroy() {
		for (ob in watchMap.values) {
			act.contentResolver.unregisterContentObserver(ob)
		}
		watchMap.clear()
		MsgCenter.remove(this)
		dev.entao.ui.activities.Pages.onDestroy(this)
		super.onDestroy()
	}

	override fun onMsg(msg: Msg) {
	}

	fun alert(msg: String) {
		DialogX.alert(this.act, msg)
	}

	companion object {
		private const val TAKE_PHOTO = 988
		private const val TAKE_VIDEO = 989
		private const val PICK_PHOTO = 977
		private const val CROP_PHOTO = 966
	}

}
