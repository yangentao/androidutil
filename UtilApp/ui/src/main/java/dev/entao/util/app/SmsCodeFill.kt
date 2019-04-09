@file:Suppress("UNUSED_PARAMETER")

package dev.entao.util.app

import android.net.Uri
import android.widget.TextView
import dev.entao.appbase.sql.GE
import dev.entao.appbase.sql.RowData
import dev.entao.appbase.sql.UriQuery
import dev.entao.appbase.sql.listRow_
import java.util.regex.Pattern

/**
 * Created by yangentao on 16/3/1.
 */
object SmsCodeFill {
	private val INBOX_SMS_URI = Uri.parse("content://sms/inbox")

	fun getSmsSince(time: Long): List<RowData> {
		return  UriQuery(INBOX_SMS_URI).where("date" GE time).desc("date").query()?.listRow_ ?: return emptyList()
	}

	fun searchSmsCode(textView: TextView, sinceTime: Long = System.currentTimeMillis()) {
//		TaskUtil.repeatFore(30, 2000, object : RepeatCallback() {
//
//			override fun onRepeat(index: Int, value: Long): Boolean {
//				val ls = getSmsSince(sinceTime)
//				ls.forEach {
//					val code = matchCode(it.str("body"))
//					val n = code?.length ?: 0
//					if (n in 4..6) {
//						textView.text = code
//						return false
//					}
//				}
//				return true
//			}
//		})

	}

	private fun matchCode(body: String?): String? {
		if (body != null) {
			val p = Pattern.compile("(\\d{4,6})")
			val m = p.matcher(body)
			if (m.find() && m.groupCount() >= 1) {
				return m.group()
			}
		}
		return null
	}

}
