package yet.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import yet.ui.activities.Pages
import yet.ui.ext.HeightFlex
import yet.ui.ext.LParam
import yet.ui.ext.WidthFill
import yet.ui.ext.genId

open class WebPage : TitlePage() {

	lateinit var webView: WebView
	protected var rootUrl: String? = null
	protected var title: String? = null


	open fun onLoadWebUrl(view: WebView, url: String) {

	}

	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title(title ?: "")
		webView = WebView(context).genId()
		contentView.addView(webView, LParam.WidthFill.HeightFlex)

		webView.settings.javaScriptEnabled = true
		webView.settings.domStorageEnabled = true


		webView.webViewClient = object : WebViewClient() {
			override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
				view.loadUrl(url)
				onLoadWebUrl(view, url)
				return true
			}

			override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
				spinProgressDlg.showLoading()
			}

			override fun onPageFinished(view: WebView, url: String) {
				spinProgressDlg.dismiss()
			}

		}
		webView.webChromeClient = object : WebChromeClient() {
			override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
				return super.onJsAlert(view, url, message, result)
			}
		}


		if (rootUrl != null) {
			webView.loadUrl(rootUrl)
		}
	}


	override fun onBackPressed(): Boolean {
		if (webView.canGoBack()) {
			webView.goBack()
			return true
		} else {
			return super.onBackPressed()
		}
	}

	fun loadAsset(assetPath: String) {
		webView.loadUrl("file:///android_asset/" + assetPath)
	}

	companion object {
		fun open(context: Context, title: String, url: String): WebPage {
			val page = WebPage()
			page.rootUrl = url
			page.title = title
			Pages.open(context, page)
			return page
		}

		fun openAsset(context: Context, title: String, assetPath: String): WebPage {
			val page = WebPage()
			page.rootUrl = "file:///android_asset/$assetPath"
			page.title = title
			Pages.open(context, page)
			return page
		}
	}

}
