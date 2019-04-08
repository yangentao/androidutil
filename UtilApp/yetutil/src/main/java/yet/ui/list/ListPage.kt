package yet.ui.list

import android.content.Context
import android.database.DataSetObserver
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import android.widget.*
import yet.ui.ext.*
import yet.ui.list.views.TextItemView
import yet.ui.page.TitlePage
import yet.ui.viewcreator.*
import yet.util.Task
import kotlin.reflect.KClass

/**
 * @param
 */
abstract class ListPage : TitlePage() {

	val anyAdapter = AnyAdapter()
	lateinit var listViewParent: RelativeLayout
		private set
	lateinit var listView: ListView
		private set
	lateinit var emptyView: TextView
		private set
	lateinit var refreshLayout: SwipeRefreshLayout

	var autoShowRefreshingAnimator = true
	var withSearchEdit = false
	var searchEdit: EditText? = null

	var itemTypes: List<KClass<*>> = emptyList()


	fun enablePullRefresh(enable: Boolean = true) {
		refreshLayout.isEnabled = enable
	}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {

		if (withSearchEdit) {
			searchEdit = contentView.editX(LParam.WidthFill.HeightEditSearch.margins(15, 5, 15, 5)) {
				styleSearch()
				singleLine()
				imeSearch {
					it.hideInputMethod()
					val s = searchEdit?.textS?.trim() ?: ""
					onSearchTextChanged(s)
				}
			}
		}

		listViewParent = contentView.relative(LParam.WidthFill.HeightFlex) {
			refreshLayout = swipeRefresh(RParam.Fill) {
				setOnRefreshListener {
					onPullRefresh()
				}
				setColorSchemeResources(
						android.R.color.holo_green_dark,
						android.R.color.holo_blue_dark,
						android.R.color.holo_orange_dark,
						android.R.color.holo_red_dark
				)
				listView = listView(MParam.Fill) {}
			}
			emptyView = textView(RParam.Fill) {
				textS = "没有内容"
				gravityCenter().textSizeB().gone()
			}
			listView.emptyView = emptyView

		}

		anyAdapter.onRequestItems = this::onRequestItems
		anyAdapter.onItemsRefreshed = this::onItemsRefreshed
		anyAdapter.onNewView = { c, p ->
			val v = this@ListPage.onNewView(c, p)
			packNewView(c, v, p)
		}
		anyAdapter.onBindView = { v, p ->
			val iv = unpackBindView(v, p)
			this@ListPage.onBindView(iv, p)
		}
		anyAdapter.typeCount = if (itemTypes.isEmpty()) 1 else itemTypes.size
		anyAdapter.onViewType = {
			if (itemTypes.isEmpty()) {
				0
			} else {
				itemTypes.indexOf(getItem(it)::class)
			}
		}
		beforeSetAdapter()
		listView.adapter = anyAdapter
		bindListEvents()
		enablePullRefresh(false)
	}

	open fun onSearchTextChanged(s: String) {

	}

	open fun onNewView(context: Context, position: Int): View {
		return TextItemView(context)
	}

	open fun onBindView(itemView: View, position: Int) {
		onBindItem(itemView, getItem(position))
	}

	open fun onBindItem(itemView: View, item: Any) {
		(itemView as? TextItemView)?.textS = item.toString()
	}

	private fun bindListEvents() {
		listView.adapter.registerDataSetObserver(object : DataSetObserver() {
			override fun onChanged() {
				Task.fore {
					onAdapterDataChanged()
				}
			}
		})

		listView.onItemClickListener = AdapterView.OnItemClickListener { _, view, position, _ ->
			val headCount = listView.headerViewsCount
			if (position >= headCount && position < headCount + anyAdapter.count) {
				val p = position - headCount
				if (onInterceptItemClickAdapter(view, getItem(p), p)) {
					return@OnItemClickListener
				}
			}
			ListViewUtil.click(listView, view, position, object : ListViewClickListener {
				override fun onItemClickAdapter(listView: ListView, view: View, position: Int) {
					this@ListPage.onItemClickAdapter(view, getItem(position), position)
				}

				override fun onItemClickHeader(listView: ListView, view: View, position: Int) {
					this@ListPage.onItemClickHeader(view, position)
				}

				override fun onItemClickFooter(listView: ListView, view: View, position: Int) {
					this@ListPage.onItemClickFooter(view, position)
				}

				override fun onItemClick(listView: ListView, view: View, position: Int) {
					this@ListPage.onItemClick(view, position)
				}

			})
		}
		listView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, view, position, _ ->
			val headCount = listView.headerViewsCount
			if (position >= headCount && position < headCount + anyAdapter.count) {
				val p = position - headCount
				if (onInterceptItemLongClickAdapter(view, getItem(p), p)) {
					return@OnItemLongClickListener true
				}
			}
			ListViewUtil.longClick(listView, view, position, object : ListViewLongClickListener {
				override fun onItemLongClickAdapter(listView: ListView, view: View, position: Int): Boolean {
					return this@ListPage.onItemLongClickAdapter(view, getItem(position), position)
				}

				override fun onItemLongClickHeader(listView: ListView, view: View, position: Int): Boolean {
					return this@ListPage.onItemLongClickHeader(view, position)
				}

				override fun onItemLongClickFooter(listView: ListView, view: View, position: Int): Boolean {
					return this@ListPage.onItemLongClickFooter(view, position)
				}

				override fun onItemLongClick(listView: ListView, view: View, position: Int): Boolean {
					return this@ListPage.onItemLongClick(view, position)
				}

			})
		}
	}

	fun setEmptyText(text: String) {
		Task.mainThread {
			emptyView.text = text
		}
	}

	protected open fun onPullRefresh() {
		requestItems()
	}

	fun setRefreshing(refresh: Boolean) {
		Task.mainThread {
			refreshLayout.isRefreshing = refresh
			if (refresh) {
				emptyView.text = "加载中..."
			} else {
				emptyView.text = "没有内容"
			}
		}
	}

	fun setItems(items: List<Any>) {
		anyAdapter.setItems(items)
	}

	protected fun listPos(adapterPos: Int): Int {
		return adapterPos + headerCount()
	}

	protected fun headerCount(): Int {
		return listView.headerViewsCount
	}

	protected fun adapterPos(listPos: Int): Int {
		return listPos - headerCount()
	}

	fun notifyDataSetChanged() {
		anyAdapter.notifyDataSetChanged()
	}

	fun getItem(position: Int): Any {
		return anyAdapter.getItem(position)
	}

	val itemCount: Int
		get() = anyAdapter.count


	/**
	 * 请求刷新listview数据
	 */
	fun requestItems() {
		anyAdapter.requestItems()
		if (autoShowRefreshingAnimator) {
			setRefreshing(true)
		}
	}

	open fun onRequestItems(): List<Any> {
		return emptyList()
	}

	protected open fun onItemsRefreshed() {
		setRefreshing(false)
	}


	protected open fun packNewView(context: Context, view: View, position: Int): View {
		return view
	}


	protected open fun unpackBindView(itemView: View, position: Int): View {
		return itemView
	}

	protected open fun beforeSetAdapter() {
	}

	protected open fun onAdapterDataChanged() {
	}

	protected open fun onInterceptItemClickAdapter(view: View, item: Any, adapterPosition: Int): Boolean {
		return false
	}

	protected open fun onInterceptItemLongClickAdapter(view: View, item: Any, adapterPosition: Int): Boolean {
		return false
	}


	open fun onItemClickAdapter(view: View, item: Any, position: Int) {
	}

	open fun onItemClickHeader(view: View, position: Int) {

	}

	open fun onItemClickFooter(view: View, position: Int) {

	}

	open fun onItemClick(view: View, position: Int) {

	}

	open fun onItemLongClickAdapter(view: View, item: Any, position: Int): Boolean {
		return false
	}

	open fun onItemLongClickHeader(view: View, position: Int): Boolean {
		return false
	}

	open fun onItemLongClickFooter(view: View, position: Int): Boolean {
		return false
	}

	open fun onItemLongClick(view: View, position: Int): Boolean {
		return false
	}

}
