package dev.entao.base.ex

import android.util.Base64

/**
 * Created by entaoyang@163.com on 2016-10-07.
 */

fun Base64.encode(data: ByteArray): String {
	val arr = Base64.encode(data, Base64.DEFAULT)
	return String(arr)
}

fun Base64.encodeSafe(data: ByteArray): String {
	val arr = Base64.encode(data, Base64.URL_SAFE)
	return String(arr)
}

fun Base64.decode(data:ByteArray):ByteArray {
	return Base64.decode(data, Base64.DEFAULT)
}
fun Base64.decodeSafe(data:ByteArray):ByteArray {
	return Base64.decode(data, Base64.URL_SAFE)
}

fun Base64.decode(s:String):ByteArray {
	return Base64.decode(s.toByteArray(Charsets.ISO_8859_1), Base64.DEFAULT)
}
fun Base64.decodeSafe(s:String):ByteArray {
	return Base64.decode(s.toByteArray(Charsets.ISO_8859_1), Base64.URL_SAFE)
}