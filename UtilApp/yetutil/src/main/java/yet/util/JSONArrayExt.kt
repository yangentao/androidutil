package yet.util

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by entaoyang@163.com on 16/7/20.
 */

fun JSONArray.each(block: (JSONObject) -> Unit) {
	this.eachObject(block)
}

fun JSONArray.eachBool(block: (Boolean) -> Unit) {
	for (n in 0..this.length() - 1) {
		if (!this.isNull(n)) {
			val jo = this.getBoolean(n)
			block(jo)
		}
	}
}

fun JSONArray.eachInt(block: (Int) -> Unit) {
	for (n in 0..this.length() - 1) {
		if (!this.isNull(n)) {
			val jo = this.getInt(n)
			block(jo)
		}
	}
}

fun JSONArray.eachLong(block: (Long) -> Unit) {
	for (n in 0..this.length() - 1) {
		if (!this.isNull(n)) {
			val jo = this.getLong(n)
			block(jo)
		}
	}
}

fun JSONArray.eachDouble(block: (Double) -> Unit) {
	for (n in 0..this.length() - 1) {
		if (!this.isNull(n)) {
			val jo = this.getDouble(n)
			block(jo)
		}
	}
}

fun JSONArray.eachString(block: (String) -> Unit) {
	for (n in 0..this.length() - 1) {
		if (!this.isNull(n)) {
			val jo = this.getString(n)
			block(jo)
		}
	}
}

fun JSONArray.eachObject(block: (JSONObject) -> Unit) {
	for (n in 0..this.length() - 1) {
		if (!this.isNull(n)) {
			val jo = this.getJSONObject(n)
			block(jo)
		}
	}
}

fun JSONArray.eachArray(block: (JSONArray) -> Unit) {
	for (n in 0..this.length() - 1) {
		if (!this.isNull(n)) {
			val jo = this.getJSONArray(n)
			block(jo)
		}
	}
}

