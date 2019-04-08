package yet.util.app

import android.app.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import yet.ext.UriRes
import yet.ui.res.Bmp
import yet.yson.YsonObject

@Suppress("DEPRECATION")
/**
 * 图标默认是应用程序图标
 * 标题默认是应用程序名称
 * 默认自动取消

 * @author yangentao@gmail.com
 */
class Notify(val id: Int) {
	@Suppress("DEPRECATION")
	private var builder = Notification.Builder(App.app)

	val mgr: NotificationManager by lazy { App.notificationManager }
	var defaults: Int = 0

	var channel: NotificationChannel? = null

	init {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val ch = NotificationChannel(App.packageName + "$id", "yet$id", NotificationManager.IMPORTANCE_DEFAULT)
			channel = ch
			builder = Notification.Builder(App.app, App.packageName + "$id")
		}
		autoCancel(true)
		title(App.app_name)
		iconSmall(if (smallIcon == 0) App.ic_launcher else smallIcon)
		iconLarge(if (largeIcon == 0) App.ic_launcher else largeIcon)
	}

	fun cancel() {
		mgr.cancel(id)
	}

	fun iconSmall(resId: Int): Notify {
		builder.setSmallIcon(resId)
		return this
	}

	fun iconLarge(icon: Int): Notify {
		return iconLarge(Bmp.res(icon))
	}

	fun iconLarge(icon: Bitmap): Notify {
		builder.setLargeIcon(icon)
		return this
	}


	fun title(title: String): Notify {
		builder.setContentTitle(title)
		return this
	}

	fun text(msg: String): Notify {
		builder.setContentText(msg)
		return this
	}

	fun textAndTicker(msg: String): Notify {
		text(msg)
		return ticker(msg)
	}

	/**
	 * 状态栏滚动文本
	 */
	fun ticker(ticker: String): Notify {
		builder.setTicker(ticker)
		return this
	}

	/**
	 * info在通知的时间下面, 小字, 用于状态等
	 */
	fun subText(info: String): Notify {
		builder.setSubText(info)
		return this
	}

	/**
	 * 点击时打开Activity
	 */
	fun clickActivity(cls: Class<out Activity>, yo: YsonObject): Notify {
		builder.setContentIntent(IntentHelper.pendingActivity(cls, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 点击时发出广播
	 */
	fun clickBroadcast(action: String, yo: YsonObject): Notify {
		builder.setContentIntent(IntentHelper.pendingBroadcastApp(action, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	fun clickBroadcast(yo: YsonObject): Notify {
		builder.setContentIntent(IntentHelper.pendingBroadcast(NotifyReceiver::class.java, yo))
		return this
	}

	/**
	 * 点击时启动Service
	 */
	fun clickService(cls: Class<out Service>, yo: YsonObject): Notify {
		builder.setContentIntent(IntentHelper.pendingService(cls, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 清除时启动Activity
	 */
	fun deleteActivity(cls: Class<out Activity>, yo: YsonObject): Notify {
		builder.setDeleteIntent(IntentHelper.pendingActivity(cls, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 清除时发广播
	 */
	fun deleteBroadcast(action: String, yo: YsonObject): Notify {
		builder.setDeleteIntent(IntentHelper.pendingBroadcastApp(action, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 清除时启动Service
	 */
	fun deleteService(cls: Class<out Service>, yo: YsonObject): Notify {

		builder.setDeleteIntent(IntentHelper.pendingService(cls, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 点击按钮时 打开Activity
	 */
	fun actionActivity(icon: Int, title: String, cls: Class<out Activity>, yo: YsonObject): Notify {
		builder.addAction(icon, title, IntentHelper.pendingActivity(cls, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 点击按钮时发广播
	 */
	fun actionBroadcast(icon: Int, title: String, action: String, yo: YsonObject): Notify {
		builder.addAction(icon, title, IntentHelper.pendingBroadcastApp(action, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 点击按钮时启动Service
	 */
	fun actionService(icon: Int, title: String, cls: Class<out Service>, yo: YsonObject): Notify {
		builder.addAction(icon, title, IntentHelper.pendingService(cls, PendingIntent.FLAG_UPDATE_CURRENT, yo))
		return this
	}

	/**
	 * 默认的震动
	 */
	fun vib(): Notify {
		this.defaults = this.defaults or Notification.DEFAULT_VIBRATE
		return this
	}

	/**
	 * 默认的声音
	 */
	fun sound(): Notify {
		this.defaults = this.defaults or Notification.DEFAULT_SOUND
		return this
	}

	fun sound(uri: Uri): Notify {
		builder.setSound(uri)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			channel?.setSound(uri, Notification.AUDIO_ATTRIBUTES_DEFAULT)
		}
		return this
	}


	fun soundRaw(rawResId: Int): Notify {
		sound(UriRes(rawResId))
		return this
	}

	/**
	 * 是否自动清除
	 */
	fun autoCancel(autoCancel: Boolean): Notify {
		builder.setAutoCancel(autoCancel)
		return this
	}

	/**
	 * 显示数字, 跟info在同一个位置, 互斥
	 */
	fun num(num: Int): Notify {
		builder.setNumber(num)
		return this
	}

	/**
	 * 持续的通知
	 */
	fun ongoing(ongoing: Boolean): Notify {
		builder.setOngoing(ongoing)
		return this
	}

	fun progress(max: Int, progress: Int, indeterminate: Boolean = false): Notify {
		builder.setProgress(max, progress, indeterminate)
		return this
	}

	fun whenTime(time: Long): Notify {
		builder.setWhen(time)
		return this
	}

	@Suppress("DEPRECATION")
	fun show() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val ch = channel
			if (ch != null) {
				mgr.createNotificationChannel(ch)
			}
		}
		if (defaults != 0) {
			builder.setDefaults(defaults)
		}
		val n = builder.build()
		mgr.notify(id, n)
	}

	companion object {
		var smallIcon = 0
		var largeIcon = 0

		fun cancel(id: Int) {
			App.notificationManager.cancel(id)
		}
	}
}