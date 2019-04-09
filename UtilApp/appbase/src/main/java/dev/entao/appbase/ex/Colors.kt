package dev.entao.appbase.ex

import android.graphics.Color
import dev.entao.appbase.App
import dev.entao.appbase.ex.color
import dev.entao.appbase.ex.colorParse
import dev.entao.base.Hex

object Colors {
    val BLACK = 0xFF000000.color
    val DKGRAY = 0xFF444444.color
    val GRAY = 0xFF888888.color
    val LTGRAY = 0xFFCCCCCC.color
    val WHITE = 0xFFFFFFFF.color
    val RED = 0xFFFF0000.color
    val GREEN = 0xFF00FF00.color
    val BLUE = 0xFF0000FF.color
    val YELLOW = 0xFFFFFF00.color
    val CYAN = 0xFF00FFFF.color
    val MAGENTA = 0xFFFF00FF.color
    val TRANSPARENT = 0

    val TRANS = Color.TRANSPARENT
    var LightGray = Color.LTGRAY
    var GrayMajor = 0xFFDDDDDD.color
    var Disabled = 0xFFCCCCCC.color
    val BlueMajor = 0xFF4990E2.color


    var GreenMajor = 0xFF5ABA39.color
    var RedMajor = 0xFFD83B31.color
    var OrangeMajor = 0xFFF5A623.color
    var CyanMajor = 0xFF50E3C2.color
    var PinkMajor = 0xFFFD6691.color


    var Warning = 0xFFF3A407.color


    val Theme: Int get() = App.themeColor

    var PageGray = 0xFFDDDDDD.color
    var Title = Color.WHITE
    var Fade = 0xFFFF8800.color
    var Unselected = 0xFFAAAAAA.color

    var TextColorMajor = 0xFF333333.color
    var TextColorMid = 0xFF555555.color
    var TextColorMinor = 0xFF777777.color
    var TextColor = TextColorMajor
    var TextColorUnselected = 0xFF999999.color

    var EditFocus = 0xFF38C4B0.color


    var Risk = 0xFFCF2C40.color
    var Safe = 0xFF199055.color
    var Progress = 0xFF00C864.color

    var LineGray = Color.LTGRAY


    object Green {
        var Light = 0xFF54792A.color
        var Dark = 0xFF54792A.color
    }

    fun color(value: String): Int {
        return colorParse(value)
    }

    fun rgb(r: Int, g: Int, b: Int): Int {
        return Color.rgb(r, g, b)
    }

    //0xff8800 --> "#ff8800"
    fun toStringColor(color: Int): String {
        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        if (a == 0xff) {
            return "#" + Hex.encodeByte(r) + Hex.encodeByte(g) + Hex.encodeByte(b)
        }
        return "#" + Hex.encodeByte(a) + Hex.encodeByte(r) + Hex.encodeByte(g) + Hex.encodeByte(b)
    }


}


