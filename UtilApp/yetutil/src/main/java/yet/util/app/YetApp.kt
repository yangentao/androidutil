package yet.util.app

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import dev.entao.yapp.App
import dev.entao.yog.logd
import dev.entao.yson.YsonObject
import yet.ui.activities.AppVisibleListener

/**
 * Created by yet on 2015/10/10.
 */
open class YetApp : Application(), AppVisibleListener {

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
