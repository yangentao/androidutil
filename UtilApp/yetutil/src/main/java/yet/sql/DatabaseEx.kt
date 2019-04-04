package yet.sql

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import yet.log.logd
import java.util.*


fun <R> SQLiteDatabase.trans(block: SQLiteDatabase.() -> R): R {
	var ok = true
	try {
		this.beginTransaction()
		return this.block()
	} catch (ex: Throwable) {
		ok = false
		ex.printStackTrace()
		throw ex
	} finally {
		if (ok) {
			this.setTransactionSuccessful()
		}
		this.endTransaction()
	}
}

fun SQLiteDatabase.query(sql: String): Cursor? {
	return this.query(sql, kotlin.collections.emptyList())
}

fun SQLiteDatabase.query(sql: String, args: List<Any>): Cursor? {
	return this.rawQuery(sql, args.map { it.toString() }.toTypedArray())
}

fun SQLiteDatabase.exec(sql: String, args: List<Any>): Boolean {
	try {
		this.execSQL(sql, args.toTypedArray())
		return true
	} catch (ex: Throwable) {
		return false
	}
}

fun SQLiteDatabase.existTable(tableName: String): Boolean {
	val sql = "select * from sqlite_master where type = 'table' and name = '$tableName'"
	val c = this.query(sql, kotlin.collections.emptyList()) ?: return false
	return c.use {
		c.moveToNext()
	}
}

fun SQLiteDatabase.countTable(table: String): Int {
	val c = this.query("select count(*) from '$table'", emptyList()) ?: return 0
	return c.use {
		if (it.moveToNext()) {
			it.getInt(0)
		} else {
			0
		}
	}
}

fun SQLiteDatabase.createTable(table: String, vararg columns: String) {
	this.createTable(table, columns.toList())
}

fun SQLiteDatabase.createTable(table: String, columns: List<String>) {
	val s = columns.joinToString(",")
	this.execSQL("CREATE TABLE IF NOT EXISTS $table ( $s )")
}

fun SQLiteDatabase.dropTable(table: String) {
	this.execSQL("DROP TABLE IF EXISTS $table")
}

fun SQLiteDatabase.createIndex(table: String, vararg cols: String) {
	val s1 = cols.joinToString("_")
	val s2 = cols.joinToString(",")
	this.execSQL("CREATE INDEX IF NOT EXISTS ${table}_$s1 ON $table ( $s2 )")
}

fun SQLiteDatabase.addColumn(table: String, columnDef: String) {
	this.execSQL("ALTER TABLE $table ADD COLUMN $columnDef")
}

fun SQLiteDatabase.replaceX(table: String, vararg ps: Pair<String, String>) {
	val cv = ContentValues()
	ps.forEach {
		cv.put(it.first, it.second)
	}
	this.replace(table, null, cv)
}

fun SQLiteDatabase.indexsOf(table: String): HashSet<String> {
	val all = HashSet<String>()
	val c = this.rawQuery("select name from sqlite_master where type='index' and tbl_name='$table'", null)
			?: return all
	c.listRow_.forEach {
		val s = it.str("name")
		if (s != null) {
			all.add(s)
		}
	}
	return all
}

fun SQLiteDatabase.tableInfo(tableName: String): ArrayList<TableInfoItem> {
	val all = ArrayList<TableInfoItem>()
	val c = this.query("PRAGMA table_info('$tableName')", kotlin.collections.emptyList())
			?: return all
	val ls = c.listRow_
	ls.forEach {
		val item = TableInfoItem()
		item.cid = it.int("cid") ?: 0
		item.name = it.str("name") ?: ""
		item.type = it.str("type") ?: ""
		item.notNull = it.int("notnull")!! != 0
		item.defaultValue = it.str("dflt_value")
		item.pk = it.int("pk")!! != 0
		all.add(item)
	}
	return all
}


fun SQLiteDatabase.tables(): HashSet<String> {
	val all = HashSet<String>()
	val c = this.query("select name from sqlite_master where type='table'")
			?: return all
	c.listRow_.forEach {
		all += it.str("name") ?: ""
	}
	return all
}

fun SQLiteDatabase.indexs(): ArrayList<Pair<String, String>> {
	val all = ArrayList<Pair<String, String>>()
	val c = this.query("select name, tbl_name from sqlite_master where type='index'") ?: return all
	c.listRow_.forEach {
		val a = it.str("name")!!
		val b = it.str("tbl_name")!!
		all += a to b
	}
	return all
}


fun SQLiteDatabase.indexInfo(indexName: String): HashSet<String> {
	val all = HashSet<String>()
	val c = this.query("PRAGMA index_info('$indexName')") ?: return all
	c.listRow_.forEach {
		all += it.str("name")!!
	}
	return all
}

fun SQLiteDatabase.dumpTable(tableName: String) {
	val c = this.query("select * from $tableName") ?: return
	val sb = StringBuilder(200)
	c.listRow_.forEach {
		sb.setLength(0)
		it.map.forEach {
			sb.append(it.value?.toString() ?: "null").append(", ")
		}
		logd(sb.toString())
	}
}