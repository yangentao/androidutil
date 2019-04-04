package yet.sql

import android.database.Cursor
import yet.yson.YsonBlob
import yet.yson.YsonNull
import yet.yson.YsonNum
import yet.yson.YsonObject
import yet.yson.YsonString
import yet.yson.YsonValue

//带下划线表示关闭Cursor
val Cursor.listObject_: List<YsonObject>
	get() {
		val ls = ArrayList<YsonObject>()
		this.use {
			while (this.moveToNext()) {
				ls += this.currentObject
			}
		}
		return ls
	}

//带下划线表示关闭Cursor
val Cursor.firstObject_: YsonObject?
	get() {
		var d: YsonObject? = null
		this.use {
			if (this.moveToNext()) {
				d = this.currentObject
			}
		}
		return d
	}

//带下划线表示关闭Cursor
val Cursor.listRow_: List<RowData>
	get() {
		val ls = ArrayList<RowData>()
		this.use {
			while (this.moveToNext()) {
				ls += this.currentRowData
			}
		}
		return ls
	}

//带下划线表示关闭Cursor
val Cursor.firstRow_: RowData?
	get() {
		var d: RowData? = null
		this.use {
			if (this.moveToNext()) {
				d = this.currentRowData
			}
		}
		return d
	}

val Cursor.currentRowData: RowData
	get() {
		return RowData.rowOf(this)
	}

val Cursor.currentObject: YsonObject
	get() {
		val map = YsonObject(32)
		val c = this
		val colCount = c.columnCount
		for (i in 0 until colCount) {
			val key = c.getColumnName(i)
			val type = c.getType(i)
			val v: YsonValue = when (type) {
				Cursor.FIELD_TYPE_NULL -> YsonNull.inst
				Cursor.FIELD_TYPE_INTEGER -> YsonNum(c.getLong(i))
				Cursor.FIELD_TYPE_FLOAT -> YsonNum(c.getDouble(i))
				Cursor.FIELD_TYPE_STRING -> YsonString(c.getString(i))
				Cursor.FIELD_TYPE_BLOB -> YsonBlob(c.getBlob(i))
				else -> YsonNull.inst
			}
			map[key] = v
		}
		return map
	}