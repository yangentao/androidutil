@file:Suppress("unused", "LocalVariableName")

package dev.entao.base.ex

import android.util.Base64
import java.util.*

/**
 * Created by entaoyang@163.com on 16/5/13.
 */

// 去掉所有空格, 剩下全数字
fun FormatPhone(tel: String?): String? {
    var phone: String = tel ?: return null
    phone = phone.filter { it in '0'..'9' }
    if (phone.length == 12 && phone.startsWith("01")) {//013812345678, 固话也可能是12位, 0531+12345678
        return phone.substring(1)
    }
    if (phone.length == 13 && phone.startsWith("86")) {//86 13812345678
        return phone.substring(2)
    }
    if (phone.length == 14 && phone.startsWith("086")) {//086 13812345678
        return phone.substring(3)
    }
    return if (phone.length >= 3) phone else null
}

val String.base64Decoded: String
    get() {
        if (this.isEmpty()) {
            return ""
        }
        val ba = Base64.decode(this, Base64.URL_SAFE)
        return String(ba, Charsets.UTF_8)
    }
val String.base64Encoded: String
    get() {
        if (this.isEmpty()) {
            return ""
        }
        return Base64.encodeToString(this.toByteArray(), Base64.URL_SAFE)
    }

fun String.substr(from: Int, size: Int): String {
    val a = if (from >= 0) {
        from
    } else 0
    val b = if (a + size < this.length) {
        a + size
    } else {
        this.length
    }
    return this.substring(a, b)
}

fun String.escapeXML(): String {
    return this.replaceChars('<' to "&lt;", '>' to "&gt;", '&' to "&amp;", '"' to "&quot;", '\'' to "&apos;")
}

fun String.replaceChars(vararg charValuePair: Pair<Char, String>): String {
    val sb = StringBuilder(this.length + 8)
    for (ch in this) {
        val p = charValuePair.find { it.first == ch }
        if (p != null) {
            sb.append(p.second)
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}


//"abcd=defg-123".substringBetween('=','-') => "defg"
//"abcd=defg=123".substringBetween('=','=') => "defg"
//"abcd==123".substringBetween('=','=') => ""
//"abcd=123".substringBetween('=','=') => null
fun String.substringBetween(a: Char, b: Char): String? {
    val nA = this.indexOf(a)
    if (nA >= 0) {
        val nB = this.indexOf(b, nA + 1)
        if (nB >= 0) {
            return this.substring(nA + 1, nB)
        }
    }
    return null
}


fun String?.empty(): Boolean {
    return this == null || this.isEmpty()
}

fun String?.notEmpty(): Boolean {
    return this != null && this.isNotEmpty()
}

fun String?.emptyOr(s: String): String {
    return if (this == null || this.isEmpty()) s else this
}

fun String?.blankOr(s: String): String {
    if (this == null || this.isEmpty()) {
        return s
    }
    if (s.isBlank()) {
        return s
    }
    return this
}


fun String?.hasCharLast(ch: Char): Boolean {
    return (this?.lastIndexOf(ch) ?: -1) >= 0
}

fun String?.hasChar(ch: Char): Boolean {
    return (this?.indexOf(ch) ?: -1) >= 0
}


fun String.tail(n: Int): String {
    if (n <= 0) {
        return ""
    }
    if (this.length < n) {
        return this
    }
    return this.substring(this.length - n)
}

//分隔成长度不大于n的字符串数组
fun String.truck(n: Int): List<String> {
    val ls = ArrayList<String>()
    if (this.length <= n) {
        ls.add(this)
    } else {
        val x = this.length / n
        val y = this.length % n
        for (i in 1..x) {
            val start = (i - 1) * n
            ls.add(this.substring(start, start + n))
        }
        if (y != 0) {
            ls.add(this.substring(x * n))
        }
    }
    return ls
}


fun String.escapeHtml(): String {
    val sb = StringBuffer((this.length * 1.1).toInt())
    this.forEach {
        when (it) {
            '<' -> sb.append("&lt;")
            '>' -> sb.append("&gt;")
            '"' -> sb.append("&quot;")
            '\'' -> sb.append("&#x27;")
            '&' -> sb.append("&amp;")
            '/' -> sb.append("&#x2F;")
            else -> sb.append(it)
        }
    }
    return sb.toString()
}

fun String.escapeHtml(forView: Boolean): String {
    if (!forView) {
        return this.escapeHtml()
    }
    val sb = StringBuffer((this.length * 1.1).toInt())
    var i = 0
    val CR = 13.toChar()
    val LF = 10.toChar()
    val SP = ' '
    val BR = "<br/>"
    while (i < this.length) {
        val c = this[i]
        when (c) {
            '<' -> sb.append("&lt;")
            '>' -> sb.append("&gt;")
            '"' -> sb.append("&quot;")
            '\'' -> sb.append("&#x27;")
            '&' -> sb.append("&amp;")
            '/' -> sb.append("&#x2F;")
            SP -> {
                sb.append("&nbsp;")
            }
            CR -> {
                val nextChar: Char? = if (i + 1 < this.length) this[i + 1] else null
                if (nextChar != LF) {
                    sb.append(BR)
                }
            }
            LF -> {
                sb.append(BR)
            }
            else -> sb.append(c)
        }
        ++i
    }

    return sb.toString()
}

val String.escapedXML: String
    get() {
        return this.replaceChars('<' to "&lt;", '>' to "&gt;", '&' to "&amp;", '"' to "&quot;", '\'' to "&apos;")
    }
