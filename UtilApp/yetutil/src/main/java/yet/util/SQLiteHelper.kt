package yet.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import dev.entao.json.YsonArray
import dev.entao.json.YsonObject
import java.util.concurrent.Callable

class SQLiteHelper(context: Context, name: String, factory: CursorFactory, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

	fun beginTransaction() {
		writableDatabase.beginTransaction()
	}

	fun endTransaction() {
		writableDatabase.endTransaction()
	}

	fun setTransactionSuccessful() {
		writableDatabase.setTransactionSuccessful()
	}

	/**
	 * Callable.call返回true, 则提交; 返回false, 则回滚<br></br>
	 * 成功提交返回true, 失败返回false
	 */
	fun transaction(c: Callable<Void>): Boolean {
		try {
			beginTransaction()
			c.call()
			setTransactionSuccessful()
			return true
		} catch (e: Throwable) {
			e.printStackTrace()
		} finally {
			endTransaction()
		}
		return false
	}

	/**
	 * 错误返回-1, 否则返回行号
	 */
	fun insertOrReplace(table: String, values: ContentValues): Long {
		return insertWithOnConflict(table, values, SQLiteDatabase.CONFLICT_REPLACE)
	}

	/**
	 * 错误返回-1, 否则返回行号
	 */
	fun insertOrIgnore(table: String, values: ContentValues): Long {
		return insertWithOnConflict(table, values, SQLiteDatabase.CONFLICT_IGNORE)
	}

	/**
	 * 错误返回-1, 否则返回行号
	 */
	fun insertOrAbort(table: String, values: ContentValues): Long {
		return insertWithOnConflict(table, values, SQLiteDatabase.CONFLICT_ABORT)
	}

	/**
	 * 错误返回-1, 否则返回行号
	 */
	fun insertOrRollback(table: String, values: ContentValues): Long {
		return insertWithOnConflict(table, values, SQLiteDatabase.CONFLICT_ROLLBACK)
	}

	/**
	 * 错误返回-1, 否则返回行号
	 */
	fun insertOrFail(table: String, values: ContentValues): Long {
		return insertWithOnConflict(table, values, SQLiteDatabase.CONFLICT_FAIL)
	}

	// 2.2以上系统有这个方法, 可以直接调用
	private fun insertWithOnConflict(table: String, initialValues: ContentValues, conflictAlgorithm: Int): Long {
		return writableDatabase.insertWithOnConflict(table, null, initialValues, conflictAlgorithm)
	}

	fun queryForLong(sql: String, vararg args: String): Long {
		val c = querySql(sql, *args)
		return if (c.moveToFirst()) {
			c.getLong(0)
		} else 0L
	}

	fun queryTableForLong(table: String, where: String, vararg args: String): Long {
		val c = queryTable(table, where, *args)
		return if (c.moveToFirst()) {
			c.getLong(0)
		} else 0L
	}

	fun querySql(sql: String, vararg args: String): Cursor {
		val db = writableDatabase
		val ar = args.map { sqlEscape(it) }.toTypedArray()
		return db.rawQuery(sql, ar)
	}

	fun sqlEscape(s: String): String {
		return s
	}

	@Synchronized
	fun execSQL(sql: String, vararg bindArgs: Any) {
		val db = writableDatabase
		db.execSQL(sql, bindArgs)
	}

	@Synchronized
	fun insertTable(table: String, values: ContentValues): Long {
		val db = writableDatabase
		return db.insert(table, null, values)
	}

	@Synchronized
	fun updateTable(table: String, values: ContentValues, whereClause: String, vararg whereArgs: String): Int {
		val db = writableDatabase
		return db.update(table, values, whereClause, whereArgs)
	}

	@Synchronized
	fun deleteByRowID(tableName: String, rowid: Long): Int {
		val db = writableDatabase
		return db.delete(tableName, "_ROWID_=?", arrayOf(rowid.toString()))
	}

	fun deleteTable(table: String): Int {
		return deleteTable(table, null)
	}

	@Synchronized
	fun deleteTable(tableName: String, where: String?, vararg args: String): Int {
		val db = writableDatabase
		return db.delete(tableName, where, args)
	}

	fun countTable(table: String): Long {
		val db = readableDatabase
		return DatabaseUtils.queryNumEntries(db, table)

	}

	fun countTable(table: String, where: String, vararg whereArgs: String): Long {
		val db = readableDatabase
		val s = if (!TextUtils.isEmpty(where)) " where $where" else ""
		return DatabaseUtils.longForQuery(db, "select count(*) from $table$s", whereArgs)
	}

	fun queryTable(table: String): Cursor {
		val db = readableDatabase
		return db.query(table, null, null, null, null, null, null)
	}

	fun queryTable(table: String, where: String, vararg args: String): Cursor {
		return queryColumnsWhereN(table, null, where, *args)
	}

	fun queryColumnsWhereN(table: String, columns: Array<String>?, selection: String, vararg selectionArgs: String): Cursor {
		return queryColumnsWhere(table, columns, selection, selectionArgs)
	}

	fun queryColumnsWhere(table: String, columns: Array<String>?, selection: String, selectionArgs: Array<out String>?): Cursor {
		val db = readableDatabase
		return db.query(table, columns, selection, selectionArgs, null, null, null)
	}

	protected fun mapOne(c: Cursor): YsonObject {
		val js = YsonObject()
		val names = c.columnNames

		for (name in names) {
			val index = c.getColumnIndex(name)
			if (index >= 0) {
				val type = c.getType(index)
				try {
					when (type) {
						Cursor.FIELD_TYPE_BLOB -> throw IllegalArgumentException("blob field can not fill to JsonObject!")
					// break;
						Cursor.FIELD_TYPE_FLOAT -> js.any(name, c.getDouble(index))
						Cursor.FIELD_TYPE_INTEGER -> js.any(name, c.getLong(index))
						Cursor.FIELD_TYPE_NULL -> js.any(name, null as String?)
						Cursor.FIELD_TYPE_STRING -> js.any(name, c.getString(index))
						else -> throw IllegalArgumentException("Unknown field type!")
					}
				} catch (e: Exception) {
					e.printStackTrace()
				}

			}
		}

		return js
	}

	/**
	 * 只支持double/long/String
	 */
	fun queryTableOne(table: String, where: String, vararg args: String): YsonObject? {
		val c = queryTable(table, where, *args)
		var js: YsonObject? = null
		if (c.moveToFirst()) {
			js = mapOne(c)
		}
		c.close()
		return js
	}

	/**
	 * 只支持double/long/String
	 */
	fun queryTableMulti(table: String, where: String, vararg args: String): YsonArray {
		val c = queryTable(table, where, *args)
		// c.getCount();
		val arr = YsonArray()
		while (c.moveToNext()) {
			val js = mapOne(c)
			arr.add(js)
		}
		c.close()
		return arr
	}

	/**
	 * 只支持double/long/String, 没找到返回null
	 */
	fun queryOne(sql: String, vararg args: String): YsonObject? {
		val c = querySql(sql, *args)
		var js: YsonObject? = null
		if (c.moveToFirst()) {
			js = mapOne(c)
		}
		c.close()
		return js
	}

	/**
	 * 只支持double/long/String
	 */
	fun queryMulti(sql: String, vararg args: String): YsonArray {
		val c = querySql(sql, *args)
		// c.getCount();
		val arr = YsonArray()
		while (c.moveToNext()) {
			val js = mapOne(c)
			arr.add(js)
		}
		c.close()
		return arr
	}

	override fun onCreate(db: SQLiteDatabase) {}

	override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}
