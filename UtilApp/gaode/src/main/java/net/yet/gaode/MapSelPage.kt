package net.yet.gaode

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.AMapOptions
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.BitmapDescriptor
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.MyLocationStyle
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.RegeocodeAddress
import yet.ui.ext.Fill
import yet.ui.ext.FlexVer
import yet.ui.ext.LParam
import yet.ui.ext.ParentRight
import yet.ui.ext.ParentTop
import yet.ui.ext.RParam
import yet.ui.ext.WidthFill
import yet.ui.ext.height
import yet.ui.ext.margins
import yet.ui.ext.onClick
import yet.ui.ext.size
import yet.ui.list.SimpleListView
import yet.ui.page.TitlePage
import yet.ui.viewcreator.imageView
import yet.ui.viewcreator.relative
import yet.ui.viewcreator.simpleListView
import yet.util.Task
import yet.util.app.needPerm

/**
 * Created by entaoyang@163.com on 2018-04-08.
 */


open class MapSelPage : TitlePage() {
	lateinit var mapView: MapView
	lateinit var listView: SimpleListView
	var centerMarker: Marker? = null

	var currLoc: LatLng? = null
	var currRegeo: RegeocodeAddress? = null
	var currPoi: PoiItem? = null

	var onCallback: (MapSelResult) -> Unit = {}

	override fun onCreateContent(context: Context, contentView: LinearLayout) {
		super.onCreateContent(context, contentView)
		titleBar {
			title("位置")
			actionText("确定").onClick = {
				val p = currPoi
				if (p != null) {
					finish()
					onCallback(MapSelResult(currRegeo!!, p, currLoc!!))
				} else {
					toast("请选择位置")
				}
			}
		}
		val ap = AMapOptions()
		ap.camera(CameraPosition(GaoDe.lastLatLng ?: GaoDe.BeiJing, 15f, 0f, 0f))
		val rl = contentView.relative(LParam.WidthFill.FlexVer) { }
		mapView = MapView(activity, ap)
		rl.addView(mapView, RParam.Fill)

		rl.imageView(RParam.ParentRight.ParentTop.size(50).margins(10)) {
			setImageResource(GaoDe.resLoc)
			onClick {
				locate()
			}
		}


		listView = contentView.simpleListView(LParam.WidthFill.height(280)) {
			anyAdapter.onNewView = { c, _ ->
				val v = AoiItemView(c)
				v
			}
			anyAdapter.onBindView = { v, p ->
				val av = v as AoiItemView
				val item = getItem(p) as PoiItem
				av.nameView.text = item.title
				av.addressView.text = item.snippet
				if (currPoi === item) {
					av.checkView.setImageResource(GaoDe.resSel)
				} else {
					av.checkView.setImageResource(0)
				}
			}
			onItemClick = {
				currPoi = it as PoiItem
				listView.notifyDataSetChanged()
			}
		}


		mapView.onCreate(null)


		mapView.map.setOnMarkerClickListener {
			onMarkerClick(it)
			true
		}
		mapView.map.setOnMapLoadedListener {
			val locaStyle = MyLocationStyle()//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
			locaStyle.interval(5000)
			mapView.map.setMyLocationStyle(locaStyle)
			Task.fore {
				zoomTo(15)
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

		needPerm(Manifest.permission.ACCESS_COARSE_LOCATION) {
			val ls = MyLocationStyle()//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
			ls.interval(5000)
			mapView.map.setMyLocationStyle(ls)
			GaoDe.locate(activity) {}
		}


	}

	val centerPos: LatLng get() = mapView.map.cameraPosition.target

	fun locate() {
		GaoDe.locate(activity) {
			centerTo(it.latitude, it.longitude)
		}
	}

	open fun onCameraChanged(pos: CameraPosition) {
		GaoDe.addressAsync(activity, pos.target.latitude, pos.target.longitude, 200) {
			Task.fore {
				currRegeo = it
				currLoc = pos.target
				val ls = it.pois
				currPoi = ls.firstOrNull()
				listView.setItems(ls)
			}

		}
	}

	open fun onCameraChanging(pos: CameraPosition) {
		val p = centerPos
		centerMarker?.position = p

	}


	open fun onMapLoaded() {
		Task.foreDelay(1000) {
			val p = centerPos
			centerMarker = mapMarker(p, GaoDe.resMyPos)
		}

	}

	open fun onMarkerClick(m: Marker) {

	}

	fun mapMarker(loc: LatLng, resId: Int, title: String? = null): Marker {
		val bitmapDesc = BitmapDescriptorFactory.fromResource(resId)
		return mapMarker(loc, title, bitmapDesc)
	}

	fun mapMarker(loc: LatLng, title: String?, bitmapDesc: BitmapDescriptor = BitmapDescriptorFactory.defaultMarker()): Marker {
		val m = mapView.map.addMarker(MarkerOptions()
				.position(loc)
				.title(title)
				.anchor(0.5f, 0.5f)
				.icon(bitmapDesc)
				.draggable(false))
		return m
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

	fun zoomTo(n: Int) {
		updateMap {
			this.zoom(n.toFloat())
		}
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