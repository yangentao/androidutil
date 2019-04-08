package yet.util.imgloader

import dev.entao.yapp.App
import yet.net.Http
import yet.util.MultiHashMap
import yet.util.Sleep
import yet.util.Task
import java.io.File
import java.util.*

/**
 * Created by entaoyang@163.com on 2016-11-02.
 */

object FileDownloader {
	//下载中的文件
	private val processSet = HashSet<String>()

	private val listenMap = MultiHashMap<String, DownListener>()

	fun isDownloading(url: String): Boolean {
		return url in processSet
	}

	fun download(url: String, block: (File?) -> Unit) {
		Task.back {
			downloadSync(url, object : DownListener {
				override fun onDownload(url: String, ok: Boolean) {
					val f = ImageLocal.find(url)
					block(f)
				}
			})
		}
	}

	private fun httpDown(url: String, file: File): Boolean {
		if (url.length < 8) {//http://a.cn
			return false
		}
		var r = Http(url).download(file, null)
		var ok = r.OK && file.exists() && file.length() > 0
		if (!ok) {
			Sleep(300)
			r = Http(url).download(file, null)
			ok = r.OK && file.exists() && file.length() > 0
		}
		return ok
	}

	fun downloadSync(url: String, listener: DownListener? = null) {
		if (listener != null) {
			synchronized(listenMap) {
				listenMap.put(url, listener)
			}
		}
		if (!processSet.add(url)) {
			return
		}
		val tmp = App.files.ex.tempFile()
		val ok = httpDown(url, tmp)
		if (ok) {
			ImageLocal.put(url, tmp.absolutePath)
		} else {
			tmp.delete()
		}
		processSet.remove(url)
		Task.fore {
			val ls = synchronized(listenMap) {
				listenMap.remove(url)
			}
			if (ls != null) {
				for (l in ls) {
					l.onDownload(url, ok)
				}
			}
		}
	}

	fun retrive(url: String, block: (File?) -> Unit) {
		val f = ImageLocal.find(url)
		if (f != null) {
			block(f)
			return
		}
		download(url, block)
	}
}