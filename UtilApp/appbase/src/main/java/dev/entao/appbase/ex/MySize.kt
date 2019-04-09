package dev.entao.appbase.ex

/**
 * Created by entaoyang@163.com on 16/7/20.
 */
class MySize (width: Int = 0, height: Int = 0) {

	var width = 0
	var height = 0

	init {
		this.width = width
		this.height = height
	}


	fun area(): Int {
		return width * height
	}

	fun maxEdge(): Int {
		return Math.max(width, height)
	}
}
