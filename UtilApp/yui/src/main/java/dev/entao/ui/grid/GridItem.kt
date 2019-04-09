package dev.entao.ui.grid

import android.graphics.drawable.Drawable


class GridItem {
	var text: String = ""
	var marginSpace: Int = 5
	var imageSize: Int = 48
	var imageId: Int = 0
	var imagePressedId: Int = 0
	var drawable: Drawable? = null
	var drawablePressed: Drawable? = null
	var onItemClick: () -> Unit = {}


	fun click(block: () -> Unit) {
		this.onItemClick = block
	}
}


class GridItems {
	val ls = ArrayList<GridItem>()

	fun item(block: GridItem.() -> Unit) {
		val g = GridItem()
		g.block()
		ls += g
	}
}