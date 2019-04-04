package yet.util.app

import java.util.*

/**
 * Created by entaoyang@163.com on 2016-10-16.
 */

object Lang {

	val zh: Boolean get() {
		val lo = Locale.getDefault()
		if (lo != null) {
			return "zh" == lo.language
		}
		return false
	}

	//简体
	val zhCN: Boolean get() {
		val lo = Locale.getDefault()
		if (lo != null) {
			return "zh" == lo.language && "CN" == lo.country
		}
		return false
	}

	//繁体
	val zhOther: Boolean get() {
		val lo = Locale.getDefault()
		if (lo != null) {
			return "zh" == lo.language && "CN" != lo.country
		}
		return false
	}

	//英语
	val en: Boolean get() {
		val lo = Locale.getDefault()
		if (lo != null) {
			return "en" == lo.language
		}
		return false
	}

}