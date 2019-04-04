package net.yet.gaode

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.amap.api.location.AMapLocation
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.AMapOptions
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import yet.ui.ext.Fill
import yet.ui.ext.FlexVer
import yet.ui.ext.LParam
import yet.ui.ext.ParentBottom
import yet.ui.ext.ParentLeft
import yet.ui.ext.RParam
import yet.ui.ext.WidthFill
import yet.ui.ext.margins
import yet.ui.ext.onClick
import yet.ui.ext.size
import yet.ui.page.TitlePage
import yet.ui.viewcreator.imageView
import yet.ui.viewcreator.relative
import yet.util.Task
import yet.util.app.hasPerm
import yet.util.app.needPerm

/**
 * Created by entaoyang@163.com on 2018-04-08.
 */

open class MapPage : TitlePage() {
	lateinit var mapView: MapView
	var mapLoaded = false
	var myPos: LatLng? = null
	var myMarker: Marker? = null

	var zoomInit: Int = 15

	var myPosRes: Int = GaoDe.resMyPos
	var myPosAnchorX: Float = 0.5f
	var myPosAnchorY: Float = 0.5f

	var autoLocateMyPos = true

	var cityCode: String = ""
	var adCode: String = ""

	lateinit var mapParent: RelativeLayout


	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		mapLoaded = false
		myPos = null
		myMarker = null

		val ap = AMapOptions()
		ap.camera(CameraPosition(GaoDe.lastLatLng ?: GaoDe.BeiJing, zoomInit.toFloat(), 0f, 0f))
		mapParent = contentView.relative(LParam.WidthFill.FlexVer) { }
		mapView = MapView(activity, ap)
		mapParent.addView(mapView, RParam.Fill)

		mapParent.imageView(RParam.ParentLeft.ParentBottom.size(45).margins(10)) {
			setImageResource(GaoDe.resLoc)
			onClick {
				locate()
			}
		}
		mapView.onCreate(null)



		mapView.map.setOnMarkerClickListener {
			onMarkerClick(it)
			true
		}
		mapView.map.setOnMapLoadedListener {
			mapLoaded = true
			if (autoLocateMyPos) {
				locate()
			}
			onMapLoaded()
		}
		mapView.map.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
			override fun onCameraChangeFinish(pos: CameraPosition?) {
				if (pos != null) {
					onCameraChanged(pos)
				}
			}

			override fun onCameraChange(pos: CameraPosition?) {
				if (pos != null) {
					onCameraChanging(pos)
				}
			}

		})

		if (!hasPerm(Manifest.permission.ACCESS_COARSE_LOCATION)) {
			needPerm(Manifest.permission.ACCESS_COARSE_LOCATION) {
				if (autoLocateMyPos) {
					locate()
				}
			}
		}
	}

	val centerPos: LatLng get() = mapView.map.cameraPosition.target

	open fun onCameraChanged(pos: CameraPosition) {

	}

	open fun onCameraChanging(pos: CameraPosition) {

	}

	open fun onMapLoaded() {
	}

	open fun onMarkerClick(m: Marker) {

	}

	//anchorCenter 图标的锚点, true:图标中间;  false:图标中下
	fun mapMarker(loc: LatLng, resId: Int, title: String?, anchorX: Float, anchorY: Float): Marker {
		val bitmapDesc = BitmapDescriptorFactory.fromResource(resId)
		return mapView.map.addMarker(MarkerOptions()
				.position(loc)
				.title(title)
				.anchor(anchorX, anchorY)
				.icon(bitmapDesc)
				.draggable(false))
	}


	fun updateMap(anim: Boolean = false, block: CameraPosition.Builder.() -> Unit) {
		val old = mapView.map.cameraPosition
		val b = CameraPosition.Builder()
		b.zoom(old.zoom)
		b.bearing(old.bearing)
		b.tilt(old.tilt)
		b.target(old.target)
		b.block()
		val p = CameraUpdateFactory.newCameraPosition(b.build())
		if (anim) {
			mapView.map.animateCamera(p)
		} else {
			mapView.map.moveCamera(p)
		}
	}

	fun centerTo(lat: Double, lng: Double) {
		updateMap {
			this.target(com.amap.api.maps2d.model.LatLng(lat, lng))
		}
	}

	fun centerTo(loc: LatLng) {
		updateMap {
			this.target(loc)
		}
	}

	fun zoomTo(n: Int) {
		updateMap {
			this.zoom(n.toFloat())
		}
	}

	fun makeMyMarker(loc: LatLng): Marker {
		if (myMarker == null) {
			myMarker = mapMarker(loc, myPosRes, null, myPosAnchorX, myPosAnchorY)
		} else {
			myMarker?.position = loc
		}
		return myMarker!!
	}

	fun locate() {
		GaoDe.locate(activity) {
			val ll = LatLng(it.latitude, it.longitude)
			myPos = ll
			cityCode = it.cityCode
			adCode = it.adCode
			centerTo(ll)
			makeMyMarker(ll)
			val maploc = it
			Task.fore {
				onLocateSuccess(maploc)
			}
		}
	}

	open fun onLocateSuccess(loc: AMapLocation) {

	}

	override fun onDestroy() {
		super.onDestroy()
		mapView.onDestroy()
	}

	override fun onResume() {
		super.onResume()
		mapView.onResume()
	}

	override fun onPause() {
		super.onPause()
		mapView.onPause()
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		mapView.onSaveInstanceState(outState)
	}
}