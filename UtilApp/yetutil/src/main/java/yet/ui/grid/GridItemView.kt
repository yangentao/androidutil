package yet.ui.grid

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.*
import yet.theme.IconSize
import yet.ui.ext.*
import yet.ui.viewcreator.*

/**
 * Created by entaoyang@163.com on 2016-08-24.
 */


class GridItemView(context: Context) : RelativeLayout(context) {
	val imageView: ImageView
	val textView: TextView

	init {
		padding(10)
		val ll = linearVer(rParam().wrap().centerInParent()) {}
		ll.gravityCenter()
		imageView = ll.imageView(lParam().width(IconSize.Large).height(IconSize.Large).gravityBottomCenter()) {
			scaleCenterInside()
		}
		textView = ll.textView(lParam().wrap().gravityTopCenter()) {
			textSizeTiny()
			textColorMinor()
		}
		backColorWhiteFade()
	}

	fun imageSizeWrap() {
		val p = imageView.layoutParams as LinearLayout.LayoutParams
		p.widthWrap().heightWrap()
	}

	fun imageSize(w: Int, h: Int = w) {
		val p = imageView.layoutParams as LinearLayout.LayoutParams
		p.width(w).height(h)
	}


	fun text(text: String): GridItemView {
		textView.text = text
		return this
	}

	fun image(@DrawableRes imgId: Int): GridItemView {
		imageView.setImageResource(imgId)
		return this
	}

	fun image(d: Drawable): GridItemView {
		imageView.setImageDrawable(d)
		return this
	}

	fun setValues(text: String, @DrawableRes imgId: Int) {
		text(text)
		image(imgId)
	}

	fun setValues(text: String, @DrawableRes imgId: Int, imgSize: Int) {
		text(text)
		image(imgId)
		if (imgSize > 0) {
			imageSize(imgSize)
		}
	}

	fun setValues(text: String, d: Drawable) {
		text(text).image(d)
	}
}