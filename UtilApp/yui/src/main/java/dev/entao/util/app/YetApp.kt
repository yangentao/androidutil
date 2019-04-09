package dev.entao.util.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import dev.entao.appbase.App
import dev.entao.json.YsonObject
import dev.entao.log.logd

/**
 * Created by yet on 2015/10/10.
 */
open class YetApp : Application(), dev.entao.ui.activities.AppVisibleListener {

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
		MultiDex.install(this)
	}

	override fun onCreate() {
		super.onCreate()
		App.init(this)
	}

	override fun onEnterForeground() {
	}

	override fun onEnterBackground() {
	}

	open fun onNotifyClick(yo: YsonObject) {
		logd("onNotifyClick:", yo.toString())
	}
}
