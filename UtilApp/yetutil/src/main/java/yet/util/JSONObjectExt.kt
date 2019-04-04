package yet.util

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by entaoyang@163.com on 16/7/20.
 */


fun JSONObject.dateTime(key: String): Long {
	val s = this.string(key) ?: return 0L
	if (s.length < 6) {
		return 0L
	}
	return MyDate.parseDateTime(s)?.time ?: 0L
}

fun JSONObject.bool(key: String): Boolean? {
	return this.optBoolean(key)
}

fun JSONObject.int(key: String): Int? {
	return this.optInt(key)
}

fun JSONObject.long(key: String): Long? {
	return this.optLong(key)
}

fun JSONObject.string(key: String): String? {
	return this.optString(key)
}

fun JSONObject.double(key: String): Double? {
	val d = this.optDouble(key)
	return if (d.isNaN()) null else d
}

fun JSONObject.obj(key: String): JSONObject? {
	return this.optJSONObject(key)
}

fun JSONObject.array(key: String): JSONArray? {
	return this.optJSONArray(key)
}

fun JSONObject.putNull(key: String): JSONObject? {
	this.put(key, null)
	return this
}


fun JSONObject.bool(key: String, value: Boolean?): JSONObject? {
	if (value == null) {
		putNull(key)
	} else {
		this.put(key, value)
	}
	return this
}

fun JSONObject.int(key: String, value: Int?): JSONObject? {
	if (value == null) {
		putNull(key)
	} else {
		this.put(key, value)
	}
	return this
}

fun JSONObject.long(key: String, value: Long?): JSONObject? {
	if (value == null) {
		putNull(key)
	} else {
		this.put(key, value)
	}
	return this
}

fun JSONObject.double(key: String, value: Double?): JSONObject? {
	if (value == null) {
		putNull(key)
	} else {
		this.put(key, value)
	}
	return this
}

fun JSONObject.string(key: String, value: String?): JSONObject? {
	if (value == null) {
		putNull(key)
	} else {
		this.put(key, value)
	}
	return this
}

fun JSONObject.obj(key: String, value: JSONObject?): JSONObject? {
	if (value == null) {
		putNull(key)
	} else {
		this.put(key, value)
	}
	return this
}

fun JSONObject.array(key: String, value: JSONArray?): JSONObject? {
	if (value == null) {
		putNull(key)
	} else {
		this.put(key, value)
	}
	return this
}

