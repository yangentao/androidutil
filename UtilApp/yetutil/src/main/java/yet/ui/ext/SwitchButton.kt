package yet.ui.ext

import android.content.Context
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.widget.CheckBox
import dev.entao.appbase.ex.Colors
import dev.entao.appbase.ex.ImageStated
import yet.ui.res.Shapes

/**
 * Created by entaoyang@163.com on 16/6/4.
 */

//width:60, height:30
open class SwitchButton(context: Context) : CheckBox(context) {
	private val onLayoutChange = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
		this.post {
			resetImage()
		}
	}

	init {
		this.compoundDrawablePadding = 0
		this.addOnLayoutChangeListener(this.onLayoutChange)
	}

	override fun setChecked(checked: Boolean) {
		val old = this.isChecked
		super.setChecked(checked)
		if (old != checked) {
			resetImage()
		}
	}

	fun makeDrawDp(w: Int, h: Int): LayerDrawable {

		val dd1 = Shapes.rect {
			cornerPx = dp(h / 2)
			fillColor = Colors.WHITE
			strokeWidthPx = dp(1)
			strokeColor = Colors.LightGray
			size(w, h)
		}
		val dd2 = Shapes.rect {
			cornerPx = dp(h / 2)
			fillColor = Colors.Safe
			size(w, h)
		}
		val dd3 = Shapes.rect {
			cornerPx = dp(h / 2)
			fillColor = Colors.LightGray
			strokeWidthPx = dp(1)
			strokeColor = Colors.WHITE
			size(w, h)
		}
		val draw = ImageStated(dd1).checked(dd2).enabled(dd3, false).value

		val h2: Int = if (h <= 2) {
			1
		} else {
			h - 2
		}
		val round = Shapes.oval {
			size(h2)
			fillColor = Colors.WHITE
			if (isChecked) {
				strokeWidthPx = dp(1)
				strokeColor = Colors.LightGray
			}
		}


		val ld = LayerDrawable(arrayOf(draw, round))
		val offset = dp(w - h)
		if (this.isChecked) {
			ld.setLayerInset(1, offset, 1, 1, 1)
		} else {
			ld.setLayerInset(1, 1, 1, offset, 1)
		}


		return ld
	}

	fun resetImage() {
		this.buttonDrawable = makeDrawDp(px2dp(this.width), px2dp(this.height))
	}

	companion object {
		val WIDTH = 60
		val HEIGHT = 30
	}
}
