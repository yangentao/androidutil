package yet.util.imgloader

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dev.entao.appbase.App
import yet.ui.ext.leftImage
import yet.ui.ext.rightImage
import yet.ui.ext.topImage
import yet.ui.res.Bmp
import yet.ui.res.Res
import yet.ui.res.limited
import yet.ui.res.roundSqure
import yet.util.Task
import java.lang.ref.WeakReference

class UriImage(val uri: Uri) {

	val option = ImageOption()

	fun opt(block: ImageOption.() -> Unit): UriImage {
		option.block()
		return this
	}

	fun keyString(): String {
		return "${option.limit}-${option.quility}-${option.corner}@$uri"
	}


	fun display(view: View, block: (View, Drawable?) -> Unit) {
		val weekView = WeakReference<View>(view)
		bitmap { bmp ->
			Task.fore {
				val v = weekView.get()
				if (v != null) {
					if (bmp != null) {
						block(v, BitmapDrawable(App.resource, bmp))
					} else {
						if (option.failedImage != 0) {
							val d = Res.drawable(option.failedImage).limited(option.limit)
							block(v, d)
						} else {
							block(v, null)
						}
					}
				}
			}
		}
	}


	fun display(imageView: ImageView) {
		display(imageView) { v, d ->
			v as ImageView
			v.setImageDrawable(d)
		}
	}

	fun rightImage(textView: TextView) {
		display(textView) { v, d ->
			v as TextView
			v.rightImage(d)
		}
	}

	fun leftImage(textView: TextView) {
		display(textView) { v, d ->
			v as TextView
			v.leftImage(d)
		}
	}

	fun topImage(textView: TextView) {
		display(textView) { v, d ->
			v as TextView
			v.topImage(d)
		}
	}

	fun findCache(): Bitmap? {
		val key = keyString()
		var bmp: Bitmap? = ImageCache.find(key)
		if (bmp == null) {
			bmp = Bmp.uri(uri, option.limit, option.quility) ?: return null
			if (option.corner > 0) {
				bmp = bmp.roundSqure(option.corner)
			}
			if (option.limit < 800) {
				ImageCache.put(key, bmp)
			}
		}
		return bmp
	}

	fun bitmap(block: (Bitmap?) -> Unit) {
		val b = findCache()
		block(b)
	}

}