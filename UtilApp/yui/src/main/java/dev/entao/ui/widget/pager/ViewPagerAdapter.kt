package dev.entao.ui.widget.pager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import dev.entao.ui.ext.onClick
import dev.entao.ui.viewcreator.createImageView
import java.util.*

class ViewPagerAdapter : PagerAdapter() {

	private var items: List<Any> = ArrayList()

	var onNewView: (Context, Int) -> View = { c, _ ->
		c.createImageView()
	}
	var onDestroyItem: (View, Int) -> Unit = { _, _ -> }

	var onPageClick: (View, Int) -> Unit = { _, _ -> }

	fun setItems(ls: List<Any>) {
		this.items = ls
		this.notifyDataSetChanged()
	}

	override fun getCount(): Int {
		return items.size
	}

	fun getItem(position: Int): Any {
		return items[position]
	}

	override fun isViewFromObject(view: View, `object`: Any): Boolean {
		return view === `object`
	}

	override fun instantiateItem(container: ViewGroup, position: Int): Any {
		val view = onNewView(container.context, position)
		container.addView(view)
		view.onClick {
			this@ViewPagerAdapter.onPageClick(it, position)
		}
		return view
	}

	override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
		val v = `object` as View
		container.removeView(v)
		onDestroyItem(v, position)
	}

	override fun getItemPosition(`object`: Any): Int {
		return POSITION_NONE
	}
}
