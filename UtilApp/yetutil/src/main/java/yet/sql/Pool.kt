package yet.sql

import yet.ext.closeSafe
import yet.util.Hex
import yet.util.app.App
import java.util.*


val PoolPeek:Conn get() = Pool.peek

object Pool {
	private const val globalName = "global_database"
	private val globalConn = Conn(App.openOrCreateDatabase(globalName))


	private val stackLocal = object : ThreadLocal<Stack<Conn>>() {
		override fun initialValue(): Stack<Conn> {
			return Stack<Conn>()
		}
	}
	val peek: Conn
		get() {
			val stack = stackLocal.get()
			if (stack.isNotEmpty()) {
				return stack.peek()
			}
			return globalConn
		}


	fun <R> named(name: String, block: (Conn) -> R): R {
		val db = App.openOrCreateDatabase(name)
		val c = Conn(db)
		stackLocal.get().push(c)
		try {
			return block(c)
		} catch (t: Throwable) {
			throw t
		} finally {
			stackLocal.get().pop()
			c.closeSafe()
		}
	}

	fun <R> hex(user: String, block: (Conn) -> R): R {
		val s = Hex.encode(user.toByteArray()) + ".db"
		return named(s, block)
	}

	fun <R> global(block: (Conn) -> R): R {
		stackLocal.get().push(globalConn)
		try {
			return block(globalConn)
		} finally {
			stackLocal.get().pop()

		}
	}


}