package yet.util

import yet.ext.empty

/**
 * Created by entaoyang@163.com on 2016-08-06.
 */

class SysContact(val phone: String, var name: String?, val byTag: Boolean = false) : Comparable<SysContact> {


	//杨恩涛 15110151971 YANG EN TAO YANGENTAO YET
	val spell: String // YANG EN TAO
	val spellString: String  //YANGENTAO
	val spellShort: String //YET

	val tag: Char


	init {
		if (byTag) {
			spell = ""
			spellShort = ""
			spellString = ""
			tag = phone[0]
		} else {
			spell = Spell.get(name) ?: ""
			spellString = spell.replace("\\s+".toRegex(), "")
			sb.setLength(0)
			var pre = ' '
			for (i in 0..spell.length - 1) {
				val ch = spell[i]
				if (pre == ' ' && ch != ' ') {
					sb.append(ch)
				}
				pre = ch
			}
			spellShort = sb.toString()

			tag = if (spell.length > 0 && spell[0] >= 'A' && spell[0] <= 'Z') {
				spell[0]
			} else {
				'#'
			}
		}
	}

	val displayName: String get() {
		if (name.empty()) {
			return phone
		}
		return name!!
	}

	override fun compareTo(other: SysContact): Int {
		return spellShort.compareTo(other.spellShort)
	}


	companion object {
		private val sb = StringBuilder(64)
	}

}