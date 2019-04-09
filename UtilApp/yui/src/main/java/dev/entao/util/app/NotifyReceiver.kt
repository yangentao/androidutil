package dev.entao.util.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.entao.appbase.App

/**
 * Created by entaoyang@163.com on 2018-07-17.
 */

class NotifyReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context?, intent: Intent) {
		val yo = intent.yson ?: return
		val yetApp = App.inst as? YetApp
		yetApp?.onNotifyClick(yo)
	}

}