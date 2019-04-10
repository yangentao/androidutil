package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.log.logd
import dev.entao.ui.ext.*
import dev.entao.ui.page.TitlePage
import dev.entao.ui.viewcreator.textView

class MainPage : TitlePage() {

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar {
			title("Main")
			showBack().onClick = {
				this@MainPage.onBackPressed()
			}
			rightText("Push").onClick = {
				(activity as MainActivity).push(LoginPage())
			}
		}

		contentView.textView(LParam.WidthFill.height(300).marginY(50)){
			text = "Main"
			textColorRed()
			gravityCenter()
		}
	}


	override fun onBackPressed(): Boolean {
		logd("MainPage", "onBackPressed")
		return super.onBackPressed()
	}
}