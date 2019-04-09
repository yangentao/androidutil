package dev.entao.ui.activities

import dev.entao.ui.R


/**
 * Created by entaoyang@163.com on 16/4/15.
 */
class AnimConf {
	var startEnter = R.anim.yet_slide_in_right
	var startExit = R.anim.yet_slide_out_left
	var finishEnter = R.anim.yet_slide_in_left
	var finishExit = R.anim.yet_slide_out_right

	fun onStart(enter: Int, exit: Int): dev.entao.ui.activities.AnimConf {
		startEnter = enter
		startExit = exit
		return this
	}

	fun onFinish(enter: Int, exit: Int): dev.entao.ui.activities.AnimConf {
		finishEnter = enter
		finishExit = exit
		return this
	}

	companion object {
		val RightIn = dev.entao.ui.activities.AnimConf()
		val LeftIn = dev.entao.ui.activities.AnimConf().onStart(R.anim.yet_slide_in_left, R.anim.yet_slide_out_right).onFinish(R.anim.yet_slide_in_right, R.anim.yet_slide_out_left)
	}
}
