package yet.ui.grid

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.TextView
import dev.entao.appbase.ex.dp
import dev.entao.appbase.ex.limited
import dev.entao.appbase.ex.sized
import yet.ui.ext.*
import yet.ui.list.AnyAdapter
import yet.ui.res.D
import yet.util.Task

/**
 * Created by entaoyang@163.com on 2016-08-27.
 */

open class SimpleGridView(context: Context) : LineGridView(context) {
	var heightMost = false
	var autoColumn = false


	//dp
	var preferColumnWidth: Int = 64

	val anyAdapter = AnyAdapter()


	var onItemClick: (item: Any) -> Unit = {}

	var onLayoutChanged: (w: Int, h: Int) -> Unit = { _, _ -> }


	init {
		genId()
		padding(10)
		backColorWhite()
		numColumns = 3
		anyAdapter.onNewView = { c, _ ->
			val v = TextView(c)
			v.textSizeD()
			v.textColorMinor()
			v.gravityCenter()
			v

		}
		super.setAdapter(anyAdapter)
		this.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
			val s = anyAdapter.getItem(pos)
			if (s is GridItem) {
				s.onItemClick()
			}
			onItemClick(s)
		}
	}

	fun simpleItems(block: GridItems.() -> Unit) {
		val gi = GridItems()
		gi.block()
		anyAdapter.onBindView = { v, p ->
			val iv = v as TextView
			val item = anyAdapter.getItem(p) as GridItem
			iv.text = item.text
			if (item.imageId != 0) {
				if (item.imagePressedId == 0) {
					iv.topImage(item.imageId, item.imageSize, item.marginSpace)
				} else {
					val d = D.light(item.imageId, item.imagePressedId)
					d.sized(item.imageSize)
					iv.topImage(d, item.marginSpace)
				}
			} else if (item.drawable != null) {
				if (item.drawablePressed == null) {
					iv.topImage(item.drawable?.sized(item.imageSize), item.marginSpace)
				} else {
					val d = D.light(item.drawable!!, item.drawablePressed!!)
					d.sized(item.imageSize)
					iv.topImage(d, item.marginSpace)
				}
			}
		}
		anyAdapter.setItems(gi.ls)
	}

	fun bindRes(imgSize: Int, block: (Any) -> Pair<String, Int>) {
		anyAdapter.onBindView = { v, p ->
			val pp = block.invoke(getItem(p))
			val iv = v as TextView
			iv.text = pp.first
			iv.topImage(D.res(pp.second).limited(imgSize), 0)
		}
	}

	fun bindImage(imgSize: Int, block: (Any) -> Pair<String, Drawable>) {
		anyAdapter.onBindView = { v, p ->
			val pair = block.invoke(getItem(p))
			val iv = v as TextView
			iv.text = pair.first
			iv.topImage(pair.second.limited(imgSize), 0)
		}
	}

	fun setPairsRes(imgSize: Int, items: List<Pair<String, Int>>) {
		anyAdapter.onBindView = { v, p ->
			val item = getItem(p) as Pair<*, *>
			val iv = v as TextView
			iv.text = item.first as String
			iv.topImage(D.res(item.second as Int).limited(imgSize), 0)
		}
		anyAdapter.setItems(items)
	}

	@Suppress("UNCHECKED_CAST")
	fun setPairs(imgSize: Int, items: List<Pair<String, Drawable>>) {
		anyAdapter.onBindView = { v, p ->
			val item = getItem(p) as Pair<String, Drawable>
			val iv = v as TextView
			iv.text = item.first
			iv.topImage(item.second.limited(imgSize), 0)
		}
		anyAdapter.setItems(items)
	}

	fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}

	fun getItem(pos: Int): Any {
		return anyAdapter.getItem(pos)
	}

	val itemCount: Int
		get() {
			return anyAdapter.count
		}

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		super.onLayout(changed, l, t, r, b)
		if (changed) {
			val newWidth = r - l
			if (autoColumn && preferColumnWidth > 0) {
				val ww = newWidth - this.paddingLeft - this.paddingRight
				var cols = (ww + this.horizontalSpacing) / (dp(preferColumnWidth) + this.horizontalSpacing)
				if (cols < 1) {
					cols = 1
				}
				Task.fore {
					this.numColumns = cols
				}
			}

			onLayoutChanged(newWidth, b - t)
		}
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		if (!heightMost) {
			return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		}
		val heightSpec: Int = if (layoutParams.height == AbsListView.LayoutParams.WRAP_CONTENT) {
			View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
		} else {
			heightMeasureSpec
		}

		super.onMeasure(widthMeasureSpec, heightSpec)
	}
}