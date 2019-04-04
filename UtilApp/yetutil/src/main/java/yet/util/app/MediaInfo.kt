package yet.util.app

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import yet.sql.RowData
import yet.sql.UriQuery
import yet.sql.listRow_

/**
 * Created by entaoyang@163.com on 16/5/12.
 */

class MediaInfo(val uri: Uri) {
	var displayName: String? = null
	var mimeType: String? = null
	var path: String? = null
	var size = 0
	var width = 0
	var height = 0
	var record: RowData? = null
	var found = false


	init {
		////content://media/external/images/media/10025
		if (uri.host == "media" && uri.scheme == "content") {
			val c =  UriQuery(uri).queryOne()
			if(c != null) {
				val map = c.listRow_.firstOrNull()
				if(map != null) {
					displayName = map.str(MediaStore.MediaColumns.DISPLAY_NAME)
					mimeType = map.str(MediaStore.MediaColumns.MIME_TYPE)
					path = map.str(MediaStore.MediaColumns.DATA)
					size = map.int(MediaStore.MediaColumns.SIZE) ?: 0
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
						width = map.int(MediaStore.MediaColumns.WIDTH) ?: 0
						height = map.int(MediaStore.MediaColumns.HEIGHT) ?: 0
					}
					record = map
					found = true
				}
			}

		}
	}

}
