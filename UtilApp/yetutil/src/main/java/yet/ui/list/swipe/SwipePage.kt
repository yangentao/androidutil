package yet.ui.list.swipe

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import dev.entao.yog.Yog
import yet.ui.list.ListPage
import yet.ui.widget.Action

/**
 * 支持多选和侧滑的ListView Fragment

 * @param
 */
abstract class SwipePage : ListPage() {
	lateinit var swipeHandler: SwipeHandlerX

	override fun packNewView(context: Context, view: View, position: Int): View {
		val v = super.packNewView(context, view, position)
		if (canSwipe() && needSwipe(position)) {
			val swipeView = XSwipeItemView(context, v)
			onAddSwipeAction(swipeView, position)
			swipeView.commit()
			swipeView.onSwipeItemAction = { swipeItemView, actionView, action ->
				val pos = listView.getPositionForView(swipeItemView)
				if (pos >= 0) {
					onSwipeAction(swipeItemView, actionView, pos, action)
				}
			}
			return swipeView
		}
		return v
	}

	override fun unpackBindView(itemView: View, position: Int): View {
		if (itemView is XSwipeItemView) {
			onUpdateSwipeView(itemView, position)
			return super.unpackBindView(itemView.itemView, position)
		}
		return super.unpackBindView(itemView, position)
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		swipeHandler = SwipeHandlerX(listView)
		swipeHandler.enable(canSwipe())
	}

	/**
	 * 在这里添加Action

	 * @param swipeView
	 * *
	 * @param position
	 * *
	 * @param item
	 */
	protected abstract fun onAddSwipeAction(swipeView: XSwipeItemView, position: Int)

	/**
	 * 在这里更新Action

	 * @param swipeView
	 * *
	 * @param position
	 * *
	 * @param item
	 */
	open fun onUpdateSwipeView(swipeView: XSwipeItemView, p: Int) {
	}

	/**
	 * 某个Item是否允许swipe侧滑

	 * @param position
	 * *
	 * @return
	 */
	open fun needSwipe(position: Int): Boolean {
		return true
	}

	open fun canSwipe(): Boolean {
		return true
	}

	private fun onSwipeAction(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {
		swipeHandler.resetCurrent()
		val countAdapter = anyAdapter.count
		var pos = position
		if (pos >= 0 && pos < listView.headerViewsCount) {
			onSwipeActionHeader(swipeItemView, actionView, pos, action)
		}
		pos -= listView.headerViewsCount
		if (pos >= 0 && pos < countAdapter) {
			onSwipeActionAdapter(swipeItemView, actionView, pos, action)
		}
		pos -= countAdapter
		if (pos >= 0 && pos < listView.footerViewsCount) {
			onSwipeActionFooter(swipeItemView, actionView, pos, action)
		}
	}

	open fun onSwipeActionAdapter(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {
		Yog.d(position, action.tag)
	}

	open fun onSwipeActionHeader(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {

	}

	open fun onSwipeActionFooter(swipeItemView: XSwipeItemView, actionView: View, position: Int, action: Action) {

	}
}
