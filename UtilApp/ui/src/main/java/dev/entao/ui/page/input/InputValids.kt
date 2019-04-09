package dev.entao.ui.page.input

import android.widget.EditText
import dev.entao.base.ex.FormatPhone

/**
 * Created by entaoyang@163.com on 2018-03-30.
 */

abstract class EditValid(var info: String = "") {

	abstract fun check(ed: EditText, text: String): Boolean

	abstract fun why(label: String): String
}

class MinLengthValid(val len: Int, info: String) : EditValid(info) {


	override fun check(ed: EditText, text: String): Boolean {
		return text.length >= len
	}

	override fun why(label: String): String {
		return label + "长度需大于$len"
	}
}

class MaxLengthValid(val len: Int, info: String) : EditValid(info) {


	override fun check(ed: EditText, text: String): Boolean {
		return text.length <= len
	}

	override fun why(label: String): String {
		return label + "长度大于$len"
	}
}

class FixLengthValid(val len: Int, info: String) : EditValid(info) {


	override fun check(ed: EditText, text: String): Boolean {
		return text.length == len
	}

	override fun why(label: String): String {
		return label + "长度必须是$len"
	}
}

class NotEmptyValid(info: String) : EditValid(info) {


	override fun check(ed: EditText, text: String): Boolean {
		return text.isNotEmpty()
	}

	override fun why(label: String): String {
		return label + "不能为空"
	}
}

class NumberValid(info: String) : EditValid(info) {
	val NUMS = setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.')

	override fun check(ed: EditText, text: String): Boolean {
		return null == text.find { it !in NUMS }
	}

	override fun why(label: String): String {
		return label + "只能输入数字"
	}
}

class EmailValid(info: String) : EditValid(info) {
	val EMAIL = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z]+(\\.[a-zA-Z]+)?".toRegex()

	override fun check(ed: EditText, text: String): Boolean {
		return text.matches(EMAIL)
	}

	override fun why(label: String): String {
		return "请输入邮箱"
	}
}

class PhoneValid(info: String) : EditValid(info) {

	override fun check(ed: EditText, text: String): Boolean {
		return null != FormatPhone(text)
	}

	override fun why(label: String): String {
		return label + "请输入手机号"
	}
}

class Phone11Valid(info: String) : EditValid(info) {

	override fun check(ed: EditText, text: String): Boolean {
		val s = FormatPhone(text)
		return s != null && s.length == 11
	}

	override fun why(label: String): String {
		return label + "请输入手机号"
	}
}