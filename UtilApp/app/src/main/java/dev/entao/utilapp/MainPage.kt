package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.log.logd
import yet.ui.dialogs.alert
import yet.ui.dialogs.showInput
import yet.ui.page.TitlePage

class MainPage : TitlePage() {

    override fun onCreateContent(context: Context, contentView: LinearLayout) {
        super.onCreateContent(context, contentView)
        titleBar {
            title("Hello")
            rightText("Hello").onClick = {
                alert("Messsage", "Title")

            }
        }
    }
    fun hello(){
        showInput("Hello", "") {
            logd(it )
        }
    }
}