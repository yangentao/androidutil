package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import yet.ui.page.TitlePage

class MainPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
        }
    }
}