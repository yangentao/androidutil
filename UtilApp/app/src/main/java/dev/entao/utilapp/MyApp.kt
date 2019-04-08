package dev.entao.utilapp

import dev.entao.yog.logd
import yet.ui.widget.TitleBar
import yet.util.app.YetApp

class MyApp : YetApp() {

    override fun onCreate() {
        super.onCreate()
        TitleBar.TitleCenter = true
        val s = "abc0123"
        val a = s.map { it.toInt().toString(16) }.joinToString("")
        logd(a)
    }
}