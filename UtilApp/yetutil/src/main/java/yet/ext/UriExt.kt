package yet.ext

import android.content.ContentUris
import android.net.Uri
import android.webkit.MimeTypeMap
import dev.entao.appbase.App
import yet.util.app.MediaInfo
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by entaoyang@163.com on 2017-03-19.
 */


fun UriRes(resId: Int): Uri {
	return Uri.parse("android.resource://" + App.packageName + "/" + resId.toString())
}


val File.uri: Uri get() = Uri.fromFile(this)
fun Uri.openInputStream(): InputStream {
	if (this.scheme == "content") {
		return App.contentResolver.openInputStream(this)
	}
	return FileInputStream(this.path)
}

fun Uri.appendId(id: Long): Uri {
	return ContentUris.withAppendedId(this, id)
}

fun Uri.appendPath(pathSegment: String): Uri {
	return Uri.withAppendedPath(this, Uri.encode(pathSegment))
}

fun Uri.appendParam(key: String, value: String): Uri {
	return this.buildUpon().appendQueryParameter(key, value).build()
}

fun Uri.parseId(): Long {
	return ContentUris.parseId(this)
}


val Uri.mimeType: String?
	get() {
		return MimeOf(this)
	}
val Uri.fileName: String?
	get() {
		return FileNameOf(this)
	}
val Uri.extName: String?
	get() {
		val s = this.fileName
		if (s != null) {
			val ext = s.substringAfterLast('.', "")
			if (ext.isNotEmpty()) {
				return ext
			}
		}
		return null
	}


fun MimeOf(uri: Uri): String? {
	if (uri.scheme == "content") {
		if (uri.host == "media") {
			return MediaInfo(uri).mimeType
		}
	}
	val filename = uri.lastPathSegment
	val n = filename.lastIndexOf('.')
	if (n > 0) {
		val ext = filename.substring(n + 1)
		if (ext.isNotEmpty()) {
			return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
		}
	}
	return null
}

fun FileNameOf(uri: Uri): String? {
	if (uri.scheme == "content") {
		if (uri.host == "media") {
			return MediaInfo(uri).displayName
		}
	}
	return uri.lastPathSegment
}