package dev.entao.ui.res

import android.graphics.drawable.Drawable
import android.os.Build
import dev.entao.appbase.App

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */
object Res {
	val menu = dev.entao.ui.R.drawable.yet_menu
	val me = dev.entao.ui.R.drawable.yet_me
	val addWhite = dev.entao.ui.R.drawable.yet_add_white
	val arrowRight = dev.entao.ui.R.drawable.yet_arrow_right
	val more = dev.entao.ui.R.drawable.yet_arrow_right
	val back = dev.entao.ui.R.drawable.yet_back
	val checkbox = dev.entao.ui.R.drawable.yet_checkbox
	val checkboxChecked = dev.entao.ui.R.drawable.yet_checkbox_checked
	val dropdown = dev.entao.ui.R.drawable.yet_dropdown
	val editClear = dev.entao.ui.R.drawable.yet_edit_clear
	val imageMiss = dev.entao.ui.R.drawable.yet_image_miss
	val picAdd = dev.entao.ui.R.drawable.yet_pic_add
	val portrait = dev.entao.ui.R.drawable.yet_portrait
	val selAll = dev.entao.ui.R.drawable.yet_sel_all
	val selAllLight = dev.entao.ui.R.drawable.yet_sel_all2
	val del = dev.entao.ui.R.drawable.del

	fun str(resId: Int): String {
		return App.resource.getString(resId)
	}

	fun strArgs(resId: Int, vararg args: Any): String {
		return App.resource.getString(resId, *args)
	}

	@Suppress("DEPRECATION")
	fun color(resId: Int): Int {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			App.resource.getColor(resId, App.inst.theme)
		} else {
			App.resource.getColor(resId)
		}
	}


	@Suppress("DEPRECATION")
	fun drawable(resId: Int): Drawable {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			App.resource.getDrawable(resId, App.inst.theme)
		} else {
			App.resource.getDrawable(resId)
		}
	}

}