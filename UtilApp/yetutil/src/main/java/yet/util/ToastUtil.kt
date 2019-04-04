package yet.util

import android.content.Context
import android.widget.Toast

import yet.ui.res.Res
import yet.util.app.App

object ToastUtil {
	fun show(msg: String) {
		showLong(msg)
	}

	fun show(res: Int) {
		showLong(res)
	}

	fun showLong(msg: String) {
		show(App.app, msg, Toast.LENGTH_LONG)
	}

	fun showLong(res: Int) {
		show(App.app, res, Toast.LENGTH_LONG)
	}

	fun showLong(context: Context, msg: String) {
		show(context, msg, Toast.LENGTH_LONG)
	}

	fun showLong(context: Context, res: Int) {
		show(context, res, Toast.LENGTH_LONG)
	}

	fun showShort(msg: String) {
		show(App.app, msg, Toast.LENGTH_SHORT)
	}

	fun showShort(res: Int) {
		show(App.app, res, Toast.LENGTH_SHORT)
	}

	fun showShort(context: Context, msg: String) {
		show(context, msg, Toast.LENGTH_SHORT)
	}

	fun showShort(context: Context, res: Int) {
		show(context, res, Toast.LENGTH_SHORT)
	}

	fun show(context: Context, msg: Int, duration: Int) {
		show(context, Res.str(msg), duration)
	}

	fun show(context: Context, msg: String, duration: Int) {
		Task.fore {
			Toast.makeText(context, msg, duration).show()
		}
	}
}
