@file:Suppress("DEPRECATION")

package yet.ui.dialogs

import android.app.ProgressDialog
import android.content.Context
import net.yet.R
import yet.ui.res.Res
import yet.util.Task

@Suppress("DEPRECATION")
class ProgressUtil {
	private var context: Context? = null

	var progressDialog: ProgressDialog? = null

	constructor() {
	}

	constructor(context: Context) {
		this.context = context
	}

	fun setContext(context: Context) {
		this.context = context
	}

	/**
	 * 显示正在加载的进度条
	 */
	fun showLoading() {
		showProgress(Res.str(R.string.yet_loading))
	}

	fun showProgress(msg: String) {
		showProgress(msg, true, false)
	}

	fun showProgress(msg: String, cancelable: Boolean, cancelOnTouchOutside: Boolean): ProgressUtil {
		if (progressDialog != null && progressDialog!!.isShowing) {
			progressDialog!!.dismiss()
			progressDialog = null
		}
		if (context == null) {
			return this
		}
		progressDialog = ProgressDialog(context)
		if (msg.isNotEmpty()) {
			progressDialog!!.setMessage(msg)
		}
		progressDialog!!.setCanceledOnTouchOutside(cancelOnTouchOutside)
		progressDialog!!.setCancelable(cancelable)
		progressDialog!!.show()
		return this
	}

	@JvmOverloads fun showProgressHor(title: String, cancelable: Boolean = true, cancelOnTouchOutside: Boolean = false): ProgressUtil {
		if (progressDialog != null && progressDialog!!.isShowing) {
			progressDialog!!.dismiss()
		}
		if (context == null) {
			return this
		}
		progressDialog = ProgressDialog(context)
		if (title.isNotEmpty()) {
			progressDialog!!.setTitle(title)
		}
		progressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
		progressDialog!!.setCanceledOnTouchOutside(cancelOnTouchOutside)
		progressDialog!!.setCancelable(cancelable)
		progressDialog!!.show()
		return this
	}

	fun setMaxAsync(max: Long): ProgressUtil {
		Task.fore{
			if (progressDialog != null) {
				progressDialog!!.max = max.toInt()
			}
		}

		return this
	}

	fun setValueAsync(value: Long): ProgressUtil {
		Task.fore{
			if (progressDialog != null) {
				progressDialog!!.progress = value.toInt()
			}
		}
		return this
	}

	fun setMsgAsync(msg: String): ProgressUtil {
		Task.fore{
			if (progressDialog != null) {
				progressDialog!!.setTitle(msg)
			}
		}
		return this
	}

	/**
	 * 取消对话框显示
	 */
	fun dismiss() {
		if (progressDialog != null) {
			progressDialog!!.dismiss()
		}
	}

	fun dismissAsync() {
		Task.fore{
			if (progressDialog != null) {
				progressDialog!!.dismiss()
			}
		}
	}
}
