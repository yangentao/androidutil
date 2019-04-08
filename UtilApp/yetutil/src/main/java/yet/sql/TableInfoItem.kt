package yet.sql

import dev.entao.yson.Yson


/**
 * Created by entaoyang@163.com on 2017-03-10.
 */
//"cid": 0,
//"name": "locale",
//"type": "TEXT",
//"notnull": 0,
//"dflt_value": null,
//"pk": 0
class TableInfoItem {
	var cid: Int = 0
	var name: String = ""
	var type: String = ""
	var notNull: Boolean = false
	var defaultValue: String? = null
	var pk: Boolean = false

	override fun toString(): String {
		return Yson.toYson(this).toString()
	}
}