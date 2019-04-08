package yet.ui.ext

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.inputmethod.InputMethodManager
import dev.entao.yapp.App
import java.io.Serializable

/**
 * Created by yet on 2015-11-20.
 */


fun Context.callPhone(phone: String) {
	val ph = phone.trim()
	if (ph.length >= 3) {
		val uri = Uri.fromParts("tel", phone, null)
		val i = Intent(Intent.ACTION_CALL, uri)
		i.flags = Intent.FLAG_ACTIVITY_NEW_TASK// FLAG_RECEIVER_FOREGROUND
		try {
			this.startActivity(i)
		} catch (e: Throwable) {
		}
	}
}

fun Context.dialPhone(phone: String) {
	val ph = phone.trim()
	if (ph.length >= 3) {
		val uri = Uri.fromParts("tel", phone, null)
		val i = Intent(Intent.ACTION_DIAL, uri)
		i.flags = Intent.FLAG_ACTIVITY_NEW_TASK// FLAG_RECEIVER_FOREGROUND
		try {
			this.startActivity(i)
		} catch (e: Throwable) {
		}
	}
}


fun Activity.hideInputMethod() {
	val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
	val v = this.currentFocus ?: return
	if (imm.isActive) {
		imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
	}
}


fun Context.viewImage(uri: Uri) {
	this.viewAction(uri, "image/*")
}

fun Context.viewAction(uri: Uri, dataType: String) {
	val intent = Intent()
	intent.action = android.content.Intent.ACTION_VIEW
	intent.setDataAndType(uri, dataType)
	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
	this.openActivity(intent)
}


fun <T : Fragment> T.withArguments(vararg params: Pair<String, Any>): T {
	arguments = bundleOf(*params)
	return this
}

fun bundleOf(vararg params: Pair<String, Any>): Bundle {
	val b = Bundle()
	for (p in params) {
		val (k, v) = p
		when (v) {
			is Boolean -> b.putBoolean(k, v)
			is Byte -> b.putByte(k, v)
			is Char -> b.putChar(k, v)
			is Short -> b.putShort(k, v)
			is Int -> b.putInt(k, v)
			is Long -> b.putLong(k, v)
			is Float -> b.putFloat(k, v)
			is Double -> b.putDouble(k, v)
			is String -> b.putString(k, v)
			is CharSequence -> b.putCharSequence(k, v)
			is Parcelable -> b.putParcelable(k, v)
			is Serializable -> b.putSerializable(k, v)
			is BooleanArray -> b.putBooleanArray(k, v)
			is ByteArray -> b.putByteArray(k, v)
			is CharArray -> b.putCharArray(k, v)
			is DoubleArray -> b.putDoubleArray(k, v)
			is FloatArray -> b.putFloatArray(k, v)
			is IntArray -> b.putIntArray(k, v)
			is LongArray -> b.putLongArray(k, v)
			is Array<*> -> {
				@Suppress("UNCHECKED_CAST")
				when {
					v.isArrayOf<Parcelable>() -> b.putParcelableArray(k, v as Array<out Parcelable>)
					v.isArrayOf<CharSequence>() -> b.putCharSequenceArray(k, v as Array<out CharSequence>)
					v.isArrayOf<String>() -> b.putStringArray(k, v as Array<out String>)
					else -> throw IllegalArgumentException("Unsupported bundle component (${v.javaClass})")
				}
			}
			is ShortArray -> b.putShortArray(k, v)
			is Bundle -> b.putBundle(k, v)
			else -> throw IllegalArgumentException("Unsupported bundle component (${v.javaClass})")
		}
	}

	return b
}


val Context.displayMetrics: android.util.DisplayMetrics
	get() = resources.displayMetrics

val Context.configuration: android.content.res.Configuration
	get() = resources.configuration

val android.content.res.Configuration.portrait: Boolean
	get() = orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

val android.content.res.Configuration.landscape: Boolean
	get() = orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

//val android.content.res.Configuration.long: Boolean
//    get() = (screenLayout and android.content.res.Configuration.SCREENLAYOUT_LONG_YES) != 0


//--------------dp------------

fun Context.dp(dp: Int): Int {
	val v = dp * displayMetrics.density + 0.5f
	return v.toInt()
}

fun Context.sp(sp: Int): Float {
	return sp * displayMetrics.scaledDensity + 0.5f
}

fun dp(dp: Int): Int {
	return (dp * App.density + 0.5f).toInt()
}

fun dp2(dp: Int?): Int? {
	if (dp != null) {
		return dp(dp)
	}
	return null
}

fun sp(sp: Int): Float {
	return sp * App.scaledDensity + 0.5f
}

fun px2dp(px: Int): Int {
	return (px / App.density + 0.5f).toInt()
}

