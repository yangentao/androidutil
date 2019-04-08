package yet.ui.activities

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import dev.entao.log.Yog
import yet.ext.extraBool
import yet.ext.extraInt
import yet.ext.hasBits
import yet.ext.removeBits
import yet.ui.page.BaseFragment
import yet.util.InMainThread
import yet.util.Msg
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

/**
 * Created by entaoyang@163.com on 2016-07-26.
 */

object Pages {
	private val KEY = "page.temp.key"
	val FULL_SCREEN = "page.fullscreen"

	private var order: Int = 1
	private val pageList = ArrayList<Pair<Int, BaseFragment>>()

	private val lastPageClass: KClass<*>?
		get() {
			val p = pageList.lastOrNull() ?: return null
			return p.second::class
		}

	fun removePage(p: BaseFragment) {
		pageList.removeAll { it.second === p }
	}

	fun onCreate(act: BaseActivity): BaseFragment? {
		val key: Int = act.intent.extraInt(KEY)
		val p = pageList.find { it.first == key }?.second
		if (p != null) {
			if (act.intent.extraBool(FULL_SCREEN)) {
				act.setWindowFullScreen()
			}
		}

		return p
	}

	fun onDestroy(page: BaseFragment) {
		pageList.removeAll { it.second === page }
	}

	fun open(context: Context, fragment: BaseFragment, block: Intent.() -> Unit = {}) {
		assert(InMainThread)
		var n = fragment.openFlag
		if (n.hasBits(Intent.FLAG_ACTIVITY_SINGLE_TOP)) {
			if (lastPageClass == fragment::class) {
				return
			}
			n = n.removeBits(Intent.FLAG_ACTIVITY_SINGLE_TOP)
		}

		val intent = Intent(context, PageActivity::class.java)
		val wColor: Int? = fragment.windowBackColor
		if (wColor != null) {
			val alpha: Int = Color.alpha(wColor)
			if (alpha != 0xff) {
				intent.component = ComponentName(context, TransPageActivity::class.java)
			}
		}

		if (n != 0) {
			intent.flags = n
		}
		val key = order++
		pageList.add(Pair(key, fragment))
		intent.putExtra(KEY, key)
		intent.block()
		context.startActivity(intent)
		val ac = fragment.activityAnim
		if (ac != null && context is Activity) {
			context.overridePendingTransition(ac.startEnter, ac.startExit)
		}
	}

	fun open(context: Context, cls: KClass<out BaseFragment>, block: Intent.() -> Unit = {}) {
		try {
			val f = cls.createInstance()
			Pages.open(context, f, block)
		} catch (e: InstantiationException) {
			e.printStackTrace()
			Yog.fatal(e)
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
			Yog.fatal(e)
		}

	}


	const val MSG_CLOSE_ALL_PAGES = "msg.pages.close.all"

	val MSG_CLOSE_PAGE = "pages.close"


	fun closePage(vararg classes: KClass<*>) {
		for (c in classes) {
			Msg(MSG_CLOSE_PAGE).clazz(c).fire()
		}
	}
}



