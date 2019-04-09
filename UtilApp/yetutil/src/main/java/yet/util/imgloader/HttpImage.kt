package yet.util.imgloader

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import dev.entao.appbase.App
import dev.entao.appbase.ex.Bmp
import dev.entao.appbase.ex.limited
import dev.entao.appbase.ex.roundSqure
import yet.ui.ext.leftImage
import yet.ui.ext.rightImage
import yet.ui.ext.topImage
import yet.ui.res.Res
import yet.util.Task
import java.lang.ref.WeakReference


class HttpImage(val url: String) {
	val option = ImageOption()


	fun opt(block: ImageOption.() -> Unit): HttpImage {
		option.block()
		return this
	}

	fun keyString(): String {
		return "${option.limit}-${option.quility}-${option.corner}@$url"
	}

	fun findCache(): Bitmap? {
		val key = keyString()
		var bmp: Bitmap? = ImageCache.find(key)
		if (bmp == null) {
			val f = ImageLocal.find(url)
			if (f != null && f.exists()) {
				bmp = Bmp.file(f, option.limit, option.quility) ?: return null
				if (option.corner > 0) {
					bmp = bmp.roundSqure(option.corner)
				}
				if (option.limit <= 800) {
					ImageCache.put(key, bmp)
				}
			}
		}
		return bmp
	}

	fun bitmap(block: (Bitmap?) -> Unit) {
		val key = keyString()
		if (option.forceDownload) {
			FileDownloader.download(url) {
				ImageCache.remove(key)
				block(findCache())
			}
		} else {
			val b = findCache()
			if (b != null) {
				block(b)
			} else {
				FileDownloader.retrive(url) {
					block(findCache())
				}
			}
		}

	}

	fun iamgeView(view: ImageView, block: (ImageView, Drawable?) -> Unit) {
		display(view) { v, d ->
			block(v as ImageView, d)
		}
	}

	fun textView(view: TextView, block: (TextView, Drawable?) -> Unit) {
		display(view) { v, d ->
			block(v as TextView, d)
		}
	}

	fun display(view: View, block: (View, Drawable?) -> Unit) {
		if (null == ImageLocal.find(url)) {
			if (option.defaultImage != 0) {
				val d = Res.drawable(option.defaultImage).limited(option.limit)
				block(view, d)
			}
		}
		val weekView = WeakReference<View>(view)
		bitmap { bmp ->
			Task.fore {
				val v = weekView.get()
				if (v != null) {
					if (bmp != null) {
						block(v, BitmapDrawable(App.resource, bmp).limited(option.limit))
					} else {
						if (option.failedImage != 0) {
							val d = Res.drawable(option.failedImage).limited(option.limit)
							block(view, d)
						} else {
							block(view, null)
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

	companion object {
		fun batch(ls: List<String>, optBlock: ImageOption.() -> Unit, callback: (List<Bitmap>) -> Unit) {
			val bls = ArrayList<Bitmap?>()
			if (ls.isEmpty()) {
				callback(emptyList())
				return
			}
			ls.forEach { url ->
				HttpImage(url).opt(optBlock).bitmap { bmp ->
					bls += bmp
					if (bls.size == ls.size) {
						callback(bls.filterNotNull())
					}
				}
			}
		}
	}
}