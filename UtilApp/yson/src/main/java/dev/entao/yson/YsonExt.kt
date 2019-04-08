@file:Suppress("unused", "MemberVisibilityCanBePrivate")
package dev.entao.yson

import yet.ext.ITextConvert

object YsonObjectText : ITextConvert {
    override val defaultValue: Any = YsonObject()
    override fun fromText(text: String): Any? {
        return YsonObject(text)
    }
}

object YsonArrayText : ITextConvert {
    override val defaultValue: Any = YsonArray()
    override fun fromText(text: String): Any? {
        return YsonArray(text)
    }
}
