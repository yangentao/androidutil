package yet.util

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by entaoyang@163.com on 16/7/20.
 */


object JSON {
	fun parseObject(s: String): JSONObject? {
		try {
			return JSONObject(s)
		} catch (ex: Exception) {
			ex.printStackTrace()
		}
		return null
	}

	fun parseArray(s: String): JSONArray? {
		try {
			return JSONArray(s)
		} catch (ex: Exception) {
			ex.printStackTrace()
		}
		return null
	}
}