package yet.util

import java.io.StringReader

object PinYin {
	private val map: HashMap<Char, String> = HashMap(30000)

	init {
		val t = Tick()
		for (s in __PinYinData) {
			parseData2(s)
		}
		t.end("Parse End ")

	}

	fun get(ch: Char): String? {
		return map[ch]
	}

	private fun parseData2(pinYinData: String) {
		val buf = StringBuilder(64)
		var code: Char = 0.toChar()
		var needCode = true
		for (ch in pinYinData) {
			if (ch == ' ') {
				continue
			}
			if (ch == '\n' || ch == '\r') {
				if (buf.isNotEmpty()) {
					map[code] = buf.toString()
					buf.setLength(0)
				}
				needCode = true
				continue
			}
			if (needCode) {
				code = ch
				needCode = false
			} else {
				buf.append(ch)
			}
		}
		if (buf.isNotEmpty()) {
			map[code] = buf.toString()
		}

	}

	private fun parseData(pinYinData: String) {
		val sr = StringReader(pinYinData)
		sr.forEachLine { line ->
			if (line.length >= 2) {
				val ch = line.first()
				val py = line.substring(1)
				map[ch] = py
			}
		}
	}

}