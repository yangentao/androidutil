package yet.util.imgloader

import android.widget.ImageView

/**
 * Created by entaoyang@163.com on 2016-11-02.
 */

fun ImageView.load(url: String, maxEdge: Int) {
	HttpImage(url).opt { limit(maxEdge) }.display(this)
}