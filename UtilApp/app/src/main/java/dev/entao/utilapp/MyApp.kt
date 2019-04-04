package dev.entao.utilapp

import yet.ui.widget.TitleBar
import yet.util.app.YetApp

class MyApp : YetApp() {

    override fun onCreate() {
        super.onCreate()
        TitleBar.TitleCenter = true
    }
}