package yet.ui.res

import android.graphics.drawable.Drawable
import android.os.Build
import yet.util.app.App

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */
object Res {
	val menu = net.yet.R.drawable.yet_menu
	val me = net.yet.R.drawable.yet_me
	val addWhite = net.yet.R.drawable.yet_add_white
	val arrowRight = net.yet.R.drawable.yet_arrow_right
	val more = net.yet.R.drawable.yet_arrow_right
	val back = net.yet.R.drawable.yet_back
	val checkbox = net.yet.R.drawable.yet_checkbox
	val checkboxChecked = net.yet.R.drawable.yet_checkbox_checked
	val dropdown = net.yet.R.drawable.yet_dropdown
	val editClear = net.yet.R.drawable.yet_edit_clear
	val imageMiss = net.yet.R.drawable.yet_image_miss
	val picAdd = net.yet.R.drawable.yet_pic_add
	val portrait = net.yet.R.drawable.yet_portrait
	val selAll = net.yet.R.drawable.yet_sel_all
	val selAllLight = net.yet.R.drawable.yet_sel_all2
	val del = net.yet.R.drawable.del

	fun str(resId: Int): String {
		return App.resource.getString(resId)
	}

	fun strArgs(resId: Int, vararg args: Any): String {
		return App.resource.getString(resId, *args)
	}

	@Suppress("DEPRECATION")
	fun color(resId: Int): Int {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			App.resource.getColor(resId, App.app.theme)
		} else {
			App.resource.getColor(resId)
		}
	}


	@Suppress("DEPRECATION")
	fun drawable(resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.resource.getDrawable(resId, App.app.theme)
		} else {
			App.resource.getDrawable(resId)
		}
	}

}