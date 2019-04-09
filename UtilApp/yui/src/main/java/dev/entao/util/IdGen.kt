package dev.entao.util

object IdGen {
	private var id = 0

	@Synchronized
	fun gen(): Int {
		return ++id
	}
}
