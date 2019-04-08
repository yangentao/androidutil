package yet.util.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by entaoyang@163.com on 2018-07-17.
 */

class NotifyReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent) {
		val yo = intent.yson ?: return
		val yetApp = App.app as? YetApp
		yetApp?.onNotifyClick(yo)
	}

}