package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.log.logd
import dev.entao.ui.dialogs.showInput
import dev.entao.ui.page.TitlePage

class MainPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightText("Hello").onClick = {
                alert("Messsage")

            }
        }
    }

    fun hello() {
        showInput("Hello", "") {
            logd(it)
        }
    }
}