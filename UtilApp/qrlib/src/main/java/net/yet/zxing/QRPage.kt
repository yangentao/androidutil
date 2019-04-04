package net.yet.zxing

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.view.KeyEvent
import android.widget.LinearLayout
import android.widget.TextView
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import yet.log.logd
import yet.ui.dialogs.showInput
import yet.ui.ext.HeightFlex
import yet.ui.ext.HeightWrap
import yet.ui.ext.WidthFill
import yet.ui.ext.WidthFlex
import yet.ui.ext.addView
import yet.ui.ext.addViewParam
import yet.ui.ext.backColor
import yet.ui.ext.clickable
import yet.ui.ext.gravityCenter
import yet.ui.ext.heightWrap
import yet.ui.ext.onClick
import yet.ui.ext.padding
import yet.ui.ext.textColorWhite
import yet.ui.ext.topImage
import yet.ui.ext.widthFill
import yet.ui.page.PageClass
import yet.ui.page.TitlePage
import yet.ui.res.ImageStated
import yet.ui.res.sized
import yet.ui.viewcreator.createLinearHorizontal
import yet.ui.viewcreator.createTextViewC

/**
 * Created by entaoyang@163.com on 2016-10-29.
 */

class QRPage : TitlePage() {

	var title:String = "二维码扫描"
	var config: ScanConfig = ScanConfig()

	lateinit var capture: CaptureManager
	lateinit var barcodeScannerView: DecoratedBarcodeView

	var inputTextView: TextView? = null
	var lightTextView: TextView? = null

	var onScanText: (String) -> Unit = {}


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		titleBar.title(title )
		barcodeScannerView = DecoratedBarcodeView(context)
		contentView.addView(barcodeScannerView) {
			WidthFill.HeightFlex
		}
		capture = CaptureManager(activity, barcodeScannerView)
		capture.onResult = {
			onScanResult(it)
		}

		capture.initializeFromIntent(config)
		capture.decode()

		val ll = createLinearHorizontal().backColor(Color.rgb(50, 50, 50)).padding(10)
		contentView.addViewParam(ll) {
			widthFill().heightWrap()
		}
		if (config.enableManualInput) {
			val tv = makeButton(R.mipmap.qr_round, R.mipmap.qr_round2)
			tv.text = "手动输入"
			ll.addView(tv) {
				WidthFlex.HeightWrap
			}
			inputTextView = tv
			tv.onClick {
				onInputCode()
			}

		}
		if (config.enableLight) {
			val tv = makeButton(R.mipmap.light, R.mipmap.light2)
			tv.text = "开灯"
			ll.addViewParam(tv) {
				WidthFlex.HeightWrap
			}
			lightTextView = tv
			tv.onClick {
				onLightToggle()
			}
		}
//		if(config.enableFromImageFile){
//
//		}
	}

	private fun onInputCode() {
		showInput("请输入编号") {
			if (it.trim().isNotEmpty()) {
				finish()
				onScanText(it.trim())
			}
		}
	}

	fun onLightToggle() {
		val b = lightTextView?.isSelected ?: false
		if (b) {
			barcodeScannerView.setTorchOff()
		} else {
			barcodeScannerView.setTorchOn()
		}
		val newState = !b
		lightTextView?.isSelected = newState
		lightTextView?.text = if (newState) "关灯" else "开灯"
	}

	private fun makeButton(@DrawableRes normal: Int, @DrawableRes pressed: Int): TextView {
		val tv = createTextViewC().textColorWhite().clickable()
		val d = ImageStated(normal).pressed(pressed).selected(pressed).value.sized(45)
		tv.topImage(d, 2)
		tv.gravityCenter()
		return tv

	}


	fun onScanResult(result: BarcodeResult) {
		val text = result.text
		logd("ScanResult:", result.text)
		onScanText(text)
	}


	override fun onResume() {
		super.onResume()
		capture.onResume()
	}

	override fun onPause() {
		super.onPause()
		capture.onPause()
	}

	override fun onDestroy() {
		super.onDestroy()
		capture.onDestroy()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		capture.onSaveInstanceState(outState)
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		capture.onRequestPermissionsResult(requestCode, permissions, grantResults)
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
		return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
	}


	companion object : PageClass<QRPage>()
}