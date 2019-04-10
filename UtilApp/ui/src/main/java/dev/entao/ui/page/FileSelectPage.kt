package dev.entao.ui.page

import android.Manifest
import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.LinearLayout
import dev.entao.appbase.App
import dev.entao.log.Yog
import dev.entao.ui.ext.act
import dev.entao.ui.list.ListPage
import dev.entao.ui.list.views.TextDetailView
import dev.entao.util.app.needPerm
import java.io.File

class FileSelectPage : ListPage() {

	var dir: File = Environment.getExternalStorageDirectory()
	lateinit var onValue: (File?) -> Unit

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar {
			title(dir.absolutePath)
			rightText(CLEAR).onClick = {
				onValue(null)
				finish()
			}
		}

		needPerm(Manifest.permission.READ_EXTERNAL_STORAGE) {
			requestItems()
		}
	}

	override fun onItemsRefreshed() {
		super.onItemsRefreshed()
		titleBar.title(dir.absolutePath)
		titleBar.commit()
	}

	override fun onNewView(context: Context, position: Int): View {
		return TextDetailView(context)
	}

	override fun onBindView(itemView: View, position: Int) {
		val item = getItem(position) as File
		val v = itemView as TextDetailView
		var s = item.name
		if (item.isDirectory) {
			s += "/"
		}
		v.setValues(s, null)
	}

	override fun onRequestItems(): List<File> {
		val files = dir.listFiles()
		val ls: List<File> = listOf(*files).filter { !it.name.startsWith(".") }
		return ls.sortedBy { it.name }
	}

	override fun beforeSetAdapter() {
		super.beforeSetAdapter()
		val v = TextDetailView(act)
		v.setValues("上级目录..", null)
		listView.addHeaderView(v)
	}


	override fun onItemClickHeader(view: View, position: Int) {
		super.onItemClickHeader(view, position)
		goUp()
	}


	override fun onBackPressed(): Boolean {
		if (dir.absolutePath == RootSD.absolutePath) {
			return super.onBackPressed()
		}
		goUp()
		return true
	}

	private fun goUp() {
		if (dir.absolutePath == RootSD.absolutePath) {
			return
		}
		val f = dir.parentFile
		if (f != null) {
			dir = f
			needPerm(Manifest.permission.READ_EXTERNAL_STORAGE) {
				requestItems()
			}
		}
	}


	override fun onItemClickAdapter(view: View, item: Any, position: Int) {
		item as File
		if (item.isDirectory) {
			dir = item
			requestItems()
		} else {
			onValue(item)
			finish()
		}
	}

	companion object {
		private val CLEAR = "清除"
		val RootSD = Environment.getExternalStorageDirectory()

		fun open(context: Context, onValue: (File?) -> Unit) {
			open(context, Environment.getExternalStorageDirectory(), onValue)
		}

		fun open(context: Context, dir: File, onValue: (File?) -> Unit) {
			if (App.debug && !dir.isDirectory) {
				Yog.fatal("应该给一个存在的目录做参数")
			}
			val page = FileSelectPage()
			page.dir = dir
			page.onValue = onValue
			dev.entao.ui.activities.Pages.open(context, page)
		}
	}

}
