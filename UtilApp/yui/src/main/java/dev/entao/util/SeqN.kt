package dev.entao.util

object SeqN {
	private var value = 0

	operator fun next(): Int {
		return ++value
	}
}

