package net.yet.gaode

import android.content.Context
import android.widget.LinearLayout
import com.amap.api.maps2d.model.LatLng
import yet.ui.activities.Pages
import yet.util.Task

/**
 * Created by entaoyang@163.com on 2018-04-08.
 */

open class LocViewPage : MapPage() {

	lateinit var locPos: LatLng
	var title: String? = null

	init {
		autoLocateMyPos = false
	}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar {
			title("查看位置")
		}
	}

	override fun onMapLoaded() {
		super.onMapLoaded()
		mapMarker(locPos, GaoDe.resPosRed, title, 0.5f, 1.0f)
		Task.fore {
			centerTo(locPos)
		}
	}

	companion object {
		fun show(context: Context, loc: LatLng, title: String? = null) {
			val p = LocViewPage()
			p.locPos = loc
			p.title = title
			Pages.open(context, p)
		}
	}

}