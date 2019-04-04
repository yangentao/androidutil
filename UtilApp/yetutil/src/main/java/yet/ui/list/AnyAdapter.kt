package yet.ui.list

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import yet.ui.ext.genId
import yet.ui.list.views.TextItemView
import yet.util.Task
import java.util.TreeSet
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

open class AnyAdapter : BaseAdapter() {
    var all: ArrayList<Any> = ArrayList()

    var inFilter: Boolean = false
        private set

    private var backup: ArrayList<Any> = ArrayList()
    val posMap = HashMap<Int, Int>(256)
    val checkPosSet = TreeSet<Int>()

    var onOrderItems: (List<Any>) -> List<Any> = { it }
    var onItemsRefreshed: () -> Unit = {}

    var typeCount: Int = 1
    var onViewType: (pos: Int) -> Int = { 0 }

    var onNewView: (context: Context, position: Int) -> View = { c, _ ->
        TextItemView(c).genId()
    }
    var onBindView: (itemView: View, pos: Int) -> Unit = { v, p ->
        onBindItem(v, getItem(p))
    }
    var onBindItem: (View, Any) -> Unit = { v, item ->
        (v as TextView).text = item.toString()
    }


    var onRequestItems: () -> List<Any> = {
        emptyList()
    }


    val isAllChecked: Boolean
        get() {
            if (count == 0) {
                return false
            }
            return if (inFilter) checkedCount == backup.size else checkedCount == count
        }

    val checkedCount: Int
        get() = checkPosSet.size

    val checkedIndexs: Set<Int>
        get() = TreeSet(checkPosSet)

    val checkedItems: ArrayList<Any>
        get() {
            val ls = ArrayList<Any>(checkPosSet.size)
            if (inFilter) {
                for (n in checkPosSet) {
                    ls += backup[n]
                }
            } else {
                for (n in checkPosSet) {
                    ls += getItem(n)
                }
            }
            return ls
        }

    fun checkPosition(position: Int, select: Boolean) {
        val p: Int = if (inFilter) posMap[position]!! else position
        if (select) {
            checkPosSet.add(p)
        } else {
            checkPosSet.remove(p)
        }
    }

    fun checkItem(item: Any) {
        val n = if (inFilter) {
            backup.indexOf(item)
        } else {
            all.indexOf(item)
        }
        checkPosition(n, true)
    }


    fun isChecked(position: Int): Boolean {
        val p = if (inFilter) posMap[position] else position
        return p in checkPosSet
    }

    fun checkAll() {
        checkPosSet.clear()
        val ls: List<Any> = if (inFilter) backup else all
        checkPosSet += ls.indices
    }

    fun clearChecked() {
        checkPosSet.clear()
    }


    fun filter(block: (Any) -> Boolean) {
        val rawList = ArrayList<Any>(if (inFilter) backup else all)
        if (!inFilter) {
            backup.clear()
            backup.addAll(all)
        }
        inFilter = true
        posMap.clear()
        val newList = ArrayList<Any>(rawList.size)
        for (i in rawList.indices) {
            val item = rawList[i]
            if (block(item)) {
                posMap[newList.size] = i
                newList += item
            }
        }
        setItemsInner(newList)
    }

    fun clearFilter() {
        posMap.clear()
        if (inFilter) {
            inFilter = false
            val ls = ArrayList(backup)
            backup.clear()
            setItems(ls)
        }
    }

    open fun setItems(items: List<Any>) {
        val ls: List<Any> = if (items === all || items === backup) {
            ArrayList(items)
        } else {
            items
        }
        inFilter = false
        backup.clear()
        posMap.clear()
        clearChecked()
        setItemsInner(ls)

    }

    private fun setItemsInner(items: List<Any>) {
        if (inFilter) {
            Task.mainThread {
                this.all.clear()
                this.all.addAll(items)
                notifyDataSetChanged()
                onItemsRefreshed()
            }
        } else {
            Task.mainThread {
                val orderItems = onOrderItems(items)
                this.all.clear()
                this.all.addAll(orderItems)
                notifyDataSetChanged()
                onItemsRefreshed()
            }
        }
    }

    fun item(p: Int): Any {
        return getItem(p)
    }

    override fun getCount(): Int {
        return all.size
    }

    override fun getItem(position: Int): Any {
        return all[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var v = convertView
        if (v == null) {
            v = onNewView(parent.context, position)
        }
        onBindView(v, position)
        return v
    }

    override fun getItemViewType(position: Int): Int {
        return onViewType(position)
    }

    override fun getViewTypeCount(): Int {
        return typeCount
    }

    // 请求数据集, 后台线程回调onRequestItems
    fun requestItems() {
        Task.back {
            val data = onRequestItems()
            Task.fore {
                setItems(data)
            }
        }
    }
}
