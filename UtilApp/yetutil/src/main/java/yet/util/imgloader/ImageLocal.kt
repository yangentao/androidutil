package yet.util.imgloader

import yet.sql.MapTable
import java.io.File

object ImageLocal {
	//url->file
	private val map = MapTable("file_downloader")

	//查找本地
	fun find(url: String): File? {
		val f = map[url] ?: return null
		val file = File(f)
		if (file.exists()) {
			return file
		}
		map.remove(url)
		return null
	}

	fun remove(url: String) {
		val f = map[url] ?: return
		File(f).delete()
		map.remove(url)
	}

	fun put(url: String, path: String) {
		map.put(url, path)
	}
}