package dev.entao.ui.ext

import dev.entao.appbase.ex.Colors

/**
 * Created by entaoyang@163.com on 2016-11-07.
 */

class GrayLineConfig {
	var size: Int = 1
	var color: Int = Colors.LineGray
	var marginLeft: Int = 0
	var marginRight: Int = 0
	var marginTop: Int = 0
	var marginBottom: Int = 0


	fun size(sz: Int): GrayLineConfig {
		this.size = sz
		return this
	}

	fun color(c: Int): GrayLineConfig {
		this.color = c
		return this
	}

	fun top(n: Int): GrayLineConfig {
		this.marginTop = n
		return this
	}

	fun left(n: Int): GrayLineConfig {
		this.marginLeft = n
		return this
	}

	fun right(n: Int): GrayLineConfig {
		this.marginRight = n
		return this
	}

	fun bottom(n: Int): GrayLineConfig {
		this.marginBottom = n
		return this
	}

	fun topBottom(n: Int): GrayLineConfig {
		this.marginTop = n
		this.marginBottom = n
		return this
	}

	fun leftRight(n: Int): GrayLineConfig {
		this.marginLeft = n
		this.marginRight = n
		return this
	}

}