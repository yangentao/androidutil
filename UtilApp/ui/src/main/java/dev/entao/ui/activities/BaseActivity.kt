package dev.entao.ui.activities

import android.app.Activity
import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import dev.entao.appbase.App
import dev.entao.appbase.ex.Colors
import dev.entao.json.YsonObject
import dev.entao.util.*
import dev.entao.util.app.Perm
import dev.entao.util.app.yson
import java.util.*

/**
 * Created by yangentao on 16/3/12.
 */

open class BaseActivity : Activity(), MsgListener {

	var fullScreen = false

	val watchMap = HashMap<Uri, ContentObserver>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		if (fullScreen) {
			setWindowFullScreen()
		}
		statusBarColorFromTheme()
		MsgCenter.listenAll(this)

		intentArg(intent)
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		intentArg(intent)
	}

	private fun intentArg(i: Intent?) {
		val yo = i?.yson ?: return
		Task.foreDelay(100) {
			onIntentYson(yo)
		}
	}

	open fun onIntentYson(yo: YsonObject) {

	}

	fun setWindowFullScreen() {
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
	}


	fun unWatch(uri: Uri) {
		val ob = watchMap[uri]
		if (ob != null) {
			contentResolver.unregisterContentObserver(ob)
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
		contentResolver.registerContentObserver(uri, true, ob)
	}

	open fun onUriChanged(uri: Uri) {

	}


	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		Perm.onPermResult(requestCode)
	}


	fun statusBarColor(color: Int) {
		val w = window ?: return
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.statusBarColor = color
		}
	}

	fun statusBarColorFromTheme() {
		val c = dev.entao.ui.MyColor(Colors.Theme)
		statusBarColor(c.multiRGB(0.7))
	}


	override fun onMsg(msg: Msg) {
		if (msg.isMsg(dev.entao.ui.activities.Pages.MSG_CLOSE_PAGE)) {
			if (this::class == msg.cls) {
				finish()
				return
			}
		}
	}


	val findRootView: View
		get() = (findViewById(android.R.id.content) as ViewGroup).getChildAt(0)

	fun toast(text: String) {
		Task.fore {
			Toast.makeText(this, text, Toast.LENGTH_LONG).show()
		}
	}


	override fun onDestroy() {
		super.onDestroy()
		MsgCenter.remove(this)
		for (ob in watchMap.values) {
			contentResolver.unregisterContentObserver(ob)
		}
		watchMap.clear()
	}


	override fun onStart() {
		dev.entao.ui.activities.BaseActivity.Companion.visiableActivityCount += 1
		if (dev.entao.ui.activities.BaseActivity.Companion.visiableActivityCount == 1) {
			val app = App.inst
			if (app is dev.entao.ui.activities.AppVisibleListener) {
				Task.fore {
					app.onEnterForeground()
				}
			}
			Msg(MsgEnterForeground).fireCurrent()
		}
		dev.entao.ui.activities.BaseActivity.Companion._topActivity = this
		super.onStart()
	}


	override fun onStop() {
		dev.entao.ui.activities.BaseActivity.Companion.visiableActivityCount -= 1
		if (dev.entao.ui.activities.BaseActivity.Companion.visiableActivityCount <= 0) {
			dev.entao.ui.activities.BaseActivity.Companion._topActivity = null
			val app = App.inst
			if (app is dev.entao.ui.activities.AppVisibleListener) {
				Task.fore {
					app.onEnterBackground()
				}
			}
			Msg(MsgEnterBackground).fireCurrent()
		}
		super.onStop()
	}

	companion object {
		val MsgEnterBackground = "enter_background"
		val MsgEnterForeground = "enter_foreground"
		var visiableActivityCount: Int = 0
			private set
		private var _topActivity: dev.entao.ui.activities.BaseActivity? = null

		val topVisibleActivity: dev.entao.ui.activities.BaseActivity?
			get() {
				if (dev.entao.ui.activities.BaseActivity.Companion.visiableActivityCount > 0) {
					return dev.entao.ui.activities.BaseActivity.Companion._topActivity
				}
				return null
			}

	}

}
