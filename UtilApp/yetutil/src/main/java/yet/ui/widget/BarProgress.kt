package yet.ui.widget

import dev.entao.base.Progress

/**
 * Created by entaoyang@163.com on 16/6/18.
 */

class BarProgress(val bar: TopProgressBar) : Progress {
	override fun onProgressStart(total: Int) {
		bar.show(100)
	}

	override fun onProgress(current: Int, total: Int, percent: Int) {
		bar.setProgress(percent)
	}

	override fun onProgressFinish() {
		bar.hide()
	}

}