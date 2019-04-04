package yet.util.imgloader

import android.graphics.Bitmap
import android.util.LruCache

object ImageCache {
	private val cache = object : LruCache<String, Bitmap>(6 * 1024 * 1024) {
		override fun sizeOf(key: String, value: Bitmap): Int {
			return value.rowBytes * value.height
		}
	}

	fun find(key: String): Bitmap? {
		return cache.get(key)
	}

	fun put(key: String, bmp: Bitmap) {
		cache.put(key, bmp)
	}

	fun remove(key: String): Bitmap? {
		return cache.remove(key)
	}
}