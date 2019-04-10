package dev.entao.utilapp

import android.content.Context
import android.widget.LinearLayout
import dev.entao.log.logd
import dev.entao.ui.ext.*
import dev.entao.ui.page.TitlePage
import dev.entao.ui.viewcreator.textView

class LoginPage : TitlePage() {

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar {
			title("Login")
			showBack().onClick = {
				this@LoginPage.onBackPressed()

			}
			rightText("Pop").onClick = {
				(activity as MainActivity).pop()
			}
			rightText("Dialog").onClick = {
				alert("Hello")
			}
		}

		contentView.textView(LParam.WidthFill.height(300).marginY(50)) {
			text = "Login"
			textColorRed()
			gravityCenter()
		}
	}


	override fun onBackPressed(): Boolean {
		logd("LoginPage", "onBackPressed")
		fragMgr.popBackStack()
//		return super.onBackPressed()
		return true
	}
}