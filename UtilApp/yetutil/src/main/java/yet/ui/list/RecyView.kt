package yet.ui.list

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import yet.util.Task
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by yet on 2015/10/28.
 * val rv = RecyView(context)
 * rv.horizontal()
 * rv.layoutGrid(2)
 * rv.divider()
 * rv.onNewView = {
 *      val v = TextView(context)
 *      v.padding(16)
 *      v.backColorWhiteFade()
 *      v
 * }
 * rv.onBindView = { v, p ->
 *      val item = rv.getItem(p)
 *      v as TextView
 *      v.text = item as String
 * }
 * rv.onItemClick = {
 *      val item = rv.getItem(it)
 *      toast(item as String)
 * }
 *
 * rv.bindAdapter()
 * rv.setItems(ls)
 */
class RecyView(context: Context) : RecyclerView(context) {
	private val items = ArrayList<Any>()
	private val itemsBack = ArrayList<Any>()

	var itemTypes: List<KClass<*>> = emptyList()

	var onRequestItems: () -> List<Any> = { emptyList() }

	var onItemId: (Int) -> Long = { it.toLong() }

	var onNewView: (Int) -> View = { TextView(context) }
	var onBindView: (View, Int) -> Unit = { _, _ -> }
	var onItemClick: (View, Int) -> Unit = { _, _ -> }

	var orientation: Int = RecyclerView.VERTICAL

	var onItemsChanged: () -> Unit = {}

	init {
	}

	fun bindAdapter() {
		this.adapter = RecyAdapter()
	}

	fun vertical() {
		orientation = RecyclerView.VERTICAL
	}

	fun horizontal() {
		orientation = RecyclerView.HORIZONTAL
	}

	fun layoutLinear(): LinearLayoutManager {
		val lm = LinearLayoutManager(context, orientation, false)
		this.layoutManager = lm
		return lm
	}

	fun layoutGrid(columns: Int): GridLayoutManager {
		val gm = GridLayoutManager(context, columns, orientation, false)
		this.layoutManager = gm
		return gm
	}

	fun divider() {
		addItemDecoration(DividerItemDecoration(context, orientation))
	}

	fun setItems(ls: Collection<Any>) {
		this.items.clear()
		this.items.addAll(ls)
		adapter.notifyDataSetChanged()
		onItemsChanged()
	}

	fun filter(block: (Any) -> Boolean) {
		if (itemsBack.isEmpty()) {
			itemsBack.addAll(items)
		}
		val ls = itemsBack.filter(block)
		setItems(ls)
	}

	fun clearFilter() {
		if (itemsBack.isNotEmpty()) {
			this.items.clear()
			this.items.addAll(itemsBack)
			adapter.notifyDataSetChanged()
			itemsBack.clear()
			onItemsChanged()
		}
	}

	fun getItem(position: Int): Any {
		return items[position]
	}

	val itemCount: Int
		get() = items.size


	fun requestItems() {
		Task.back {
			val ls = onRequestItems()
			Task.fore {
				setItems(ls)
			}
		}
	}

	inner class RecyHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		init {
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			onItemClick(v, adapterPosition)
		}
	}

	private inner class RecyAdapter : RecyclerView.Adapter<RecyHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyHolder {
			val view = onNewView(viewType)
			return RecyHolder(view)
		}

		override fun onBindViewHolder(holder: RecyHolder, position: Int) {
			onBindView(holder.itemView, position)
		}

		override fun getItemCount(): Int {
			return items.size
		}

		override fun getItemViewType(position: Int): Int {
			if (itemTypes.isEmpty()) {
				return 0
			}
			return itemTypes.indexOf(getItem(position)::class)
		}

		override fun getItemId(position: Int): Long {
			return onItemId(position)
		}
	}
}
