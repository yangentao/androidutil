package yet.ui.activities.drawer

import android.content.Context
import android.widget.LinearLayout
import yet.ui.ext.orientationVertical

/**
 * Created by entaoyang@163.com on 16/6/27.
 */
open class NavActionPanel(context: Context) : LinearLayout(context) {
	init {
		orientationVertical()
	}
}
