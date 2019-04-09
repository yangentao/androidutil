package net.yet.kutil.ui.activities

import android.os.Bundle
import android.widget.ImageView
import dev.entao.appbase.ex.ARGB
import dev.entao.appbase.ex.RGB
import dev.entao.appbase.ex.Shapes
import dev.entao.base.Sleep
import yet.ui.activities.BaseActivity
import yet.ui.ext.*
import yet.ui.viewcreator.createRelative
import yet.ui.viewcreator.imageView
import yet.ui.viewcreator.textView
import yet.ui.widget.pager.IndicatorPager
import yet.util.Task
import yet.util.Tick

/**
 * Created by entaoyang@163.com on 16/3/12.
 */


abstract class BaseWelcomeActivity : BaseActivity() {


	/**
	 * 欢迎页的图片

	 * @return
	 */
	var welDrawable: Int = 0

	/**
	 * 介绍页的图片, 只在第一次运行时展示

	 * @return
	 */
	var guideImages: List<Int> = ArrayList<Int>()

	//毫秒
	var minTime: Long = 1000

	var showSkip = true

	private var isGuide = false

	init {
		fullScreen = true
	}

	/**
	 * 进行app的初始化/预加载工作
	 */
	protected abstract fun onBackTask()

	open fun onConfitPager(p: IndicatorPager) {

	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val rootRelView = createRelative()
		setContentView(rootRelView)
		val first = Task.isVersionFirst("ver-first-welcome")
		isGuide = first && guideImages.isNotEmpty()
		if (isGuide) {
			val ip = IndicatorPager(this)
			onConfitPager(ip)
			ip.onNewView = { c, p ->
				val item = ip.getItem(p)
				val relView = c.createRelative()
				relView.imageView(RParam.Fill) {
					scaleCenterCrop()
					setImageResource(item as Int)

				}
				relView.textView(RParam.Wrap.ParentTop.ParentRight.margins(0, 20, 20, 0)) {
					textS = "跳过"
					if (p == guideImages.size - 1) {
						textS = "进入"
					}
					padding(15, 5, 15, 5)
					backDrawable(Shapes.rect {
						fillColor = ARGB(100, 80, 80, 80)
						strokeColor = RGB(80, 80, 80)
						strokeWidthPx = 2
					})
					onClick {
						goNext()
					}
				}
				relView
			}
			ip.onPageClick = { _, p ->
				if (p == ip.getCount()) {
					goNext()
				}
			}
			rootRelView.addView(ip, RParam.Fill)
			ip.setItems(guideImages)
			return
		} else {
			val iv = ImageView(this)
			iv.setImageResource(welDrawable)
			iv.adjustViewBounds = true
			iv.scaleType = ImageView.ScaleType.FIT_XY
			rootRelView.addView(iv, RParam.Fill)
			rootRelView.textView(RParam.Wrap.ParentTop.ParentRight.margins(0, 20, 20, 0)) {
				textS = "跳过"
				padding(15, 5, 15, 5)
				backDrawable(Shapes.rect {
					fillColor = ARGB(100, 80, 80, 80)
					strokeColor = RGB(80, 80, 80)
					strokeWidthPx = 2
				})
				onClick {
					goNext()
				}
			}
		}


	}

	override fun onResume() {
		super.onResume()
		Task.back {
			val t = Tick()
			onBackTask()
			val delta = t.end("")
			if (delta < minTime) {
				Sleep(minTime - delta)
			}
			Task.fore {
				if (!isGuide) {
					goNext()
				}
			}
		}
	}

	private var nextInvoked = false
	private fun goNext() {
		if (!nextInvoked) {
			nextInvoked = true
			onNextPage()
			finish()
		}
	}

	abstract fun onNextPage()

	override fun onBackPressed() {
		// 不可退出
	}
}
