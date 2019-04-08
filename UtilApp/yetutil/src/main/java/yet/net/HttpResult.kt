package yet.net

import dev.entao.yog.loge
import dev.entao.yson.YsonArray
import dev.entao.yson.YsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import yet.ext.closeSafe
import yet.ext.notEmpty
import java.io.File
import java.io.FileOutputStream
import java.net.NoRouteToHostException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.concurrent.TimeoutException

/**
 * Created by entaoyang@163.com on 16/4/29.
 */
class HttpResult {
	var response: ByteArray? = null//如果Http.request参数给定了文件参数, 则,response是null
	var responseCode: Int = 0//200
	var responseMsg: String? = null//OK
	var contentType: String? = null//text/html;charset=utf-8
		set(value) {
			field = value
			if (value != null && value.startsWith("text/html")) {
				needDecode = true
			}
		}
	var contentLength: Int = 0//如果是gzip格式, 这个值!=response.length
	var headerMap: Map<String, List<String>>? = null
	var exception: Exception? = null

	var needDecode: Boolean = false

	val errorMsg: String?
		get() {
			val ex = exception
			return when (ex) {
				null -> httpMsgByCode(responseCode)
				is NoRouteToHostException -> "网络不可达"
				is TimeoutException -> "请求超时"
				is SocketTimeoutException -> "请求超时"
				is SocketException -> "网络错误"
				else -> ex.message
			}
		}

	var OK: Boolean = false
		get() = responseCode >= 200 && responseCode < 300

	fun OK(): Boolean {
		return OK
	}

	val contentCharset: Charset?
		get() {
			if (contentType != null) {
				val ls: List<String> = contentType!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }
				for (item in ls) {
					val ss = item.trim();
					if (ss.startsWith("charset")) {
						val charset = ss.substringAfterLast('=', "");
						if (charset.length >= 2) {
							return Charset.forName(charset)
						}
					}
				}
			}
			return null
		}

	fun needDecode(): HttpResult {
		this.needDecode = true
		return this
	}

	fun str(defCharset: Charset): String? {
		if (OK()) {
			if (response != null) {
				var s = String(response!!, contentCharset ?: defCharset)
				if (needDecode) {
					s = URLDecoder.decode(s, defCharset.name())
				}
				return s
			}
		}
		return null
	}

	fun strISO8859_1(): String? = str(Charsets.ISO_8859_1)
	fun strUtf8(): String? = str(Charsets.UTF_8)

	fun <T> castText(block: (String) -> T?): T? {
		if (OK()) {
			val s = strUtf8()
			if (s.notEmpty()) {
				try {
					return block(s!!)
				} catch (e: Exception) {
					e.printStackTrace()
				}
			}
		}
		return null
	}

	fun ysonObject(): YsonObject? {
		return castText { YsonObject(it) }
	}

	fun ysonArray(): YsonArray? {
		return castText { YsonArray(it) }
	}

	fun jsonObject(): JSONObject? {
		if (OK()) {
			val s = strUtf8()
			if (s.notEmpty()) {
				try {
					return JSONObject(s)
				} catch (e: JSONException) {
					e.printStackTrace()
				}

			}
		}
		return null
	}

	fun jsonArray(): JSONArray? {
		if (OK()) {
			val s = strUtf8()
			if (s.notEmpty()) {
				try {
					return JSONArray(s)
				} catch (e: JSONException) {
					e.printStackTrace()
				}

			}
		}
		return null
	}

	fun bytes(): ByteArray? {
		if (OK()) {
			return response
		}
		return null
	}

	fun saveTo(file: File): Boolean {
		if (OK()) {
			val dir = file.parentFile
			if (dir != null) {
				if (!dir.exists()) {
					if (!dir.mkdirs()) {
						loge("创建目录失败")
						return false
					}
				}
			}
			var fos: FileOutputStream? = null
			try {
				fos = FileOutputStream(file)
				fos.write(response)
				fos.flush()
			} catch (ex: Exception) {
				ex.printStackTrace()
			} finally {
				fos.closeSafe()
			}
		}
		return false
	}

}