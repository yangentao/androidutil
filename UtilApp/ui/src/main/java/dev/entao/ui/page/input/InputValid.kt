package dev.entao.ui.page.input

import android.widget.EditText

/**
 * Created by entaoyang@163.com on 2017-06-05.
 */

class InputValid {
	val checkList = ArrayList<EditValid>()
	var label: String = ""
	var trimText: Boolean = false
	var require: Boolean = false
	var requireInfo = ""

	fun checkAll(edit: EditText): String? {
		var s = edit.text.toString()
		if (trimText) {
			s = s.trim()
		}
		if (require && s.isEmpty()) {
			return if (requireInfo.isEmpty()) {
				label + "不能为空"
			} else {
				requireInfo
			}
		}
		for (c in checkList) {
			if (!c.check(edit, s)) {
				return c.why(label)
			}
		}
		if (trimText) {
			edit.setText(s)
		}
		return null
	}

	fun trimText() {
		trimText = true
	}

	fun minLength(n: Int, info: String = "") {
		checkList.add(MinLengthValid(n, info))
	}

	fun maxLength(n: Int, info: String = "") {
		checkList.add(MaxLengthValid(n, info))
	}

	fun fixLength(n: Int, info: String = "") {
		checkList.add(FixLengthValid(n, info))
	}

	fun notEmpty(info: String = "") {
		require = true
		requireInfo = info
	}

	fun require(info: String = "") {
		notEmpty(info)
	}


	fun label(s: String?) {
		if (s == null) {
			return
		}
		var lb = s
		if (lb.endsWith('*')) {
			require()
			lb = lb.substring(0, lb.length - 1)
		}
		if (lb.startsWith("请输入")) {
			lb = lb.substringAfter("请输入")
		}
		label = lb
	}


	fun numbers(info: String = "") {
		checkList.add(NumberValid(info))
	}

	fun email(info: String = "") {
		checkList.add(EmailValid(info))
	}

	fun phone(info: String = "") {
		checkList.add(PhoneValid(info))
	}

	fun phone11(info: String = "") {
		checkList.add(Phone11Valid(info))
	}

}