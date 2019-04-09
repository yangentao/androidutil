package dev.entao.ui.list.select

import android.content.Context
import android.view.View
import dev.entao.ui.list.views.TextDetailView

abstract class TextDetailSelectPage : SelectPage() {

	override fun onNewView(context: Context, position: Int): View {
		return TextDetailView(context)
	}

}
