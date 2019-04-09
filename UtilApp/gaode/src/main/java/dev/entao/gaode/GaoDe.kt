package dev.entao.gaode

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import dev.entao.appbase.sql.MapTable
import dev.entao.util.Task

object GaoDe {
    var resLoc: Int = R.mipmap.gd_loc
    var resMyPos: Int = R.mipmap.gd_my_pos
    var resSel: Int = R.mipmap.gd_sel
    var resPosRed: Int = R.mipmap.gd_pos_red

    val BeiJing = LatLng(39.904989, 116.405285)
    val config = MapTable("gaode")
    var lastLat: Double by config
    var lastLng: Double by config

    val lastLatLng: LatLng?
        get() {
            if (config.has("lastLat")) {
                return LatLng(lastLat, lastLng)
            }
            return null
        }

    val hasLocate: Boolean get() = lastLat > 0.01

    fun locate(context: Context, block: (AMapLocation) -> Unit) {
        val client = AMapLocationClient(context)
        val opt = AMapLocationClientOption()
        opt.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
        opt.isOnceLocation = true
        opt.isNeedAddress = true
        opt.httpTimeOut = 15000
        opt.isLocationCacheEnable = true
        client.setLocationOption(opt)
        client.setLocationListener { loc ->
            if (loc != null) {
                lastLat = loc.latitude
                lastLng = loc.longitude
                block(loc)
            }
            Task.fore {
                client.onDestroy()
            }
        }
        client.startLocation()

    }

    fun address(context: Context, lat: Double, lng: Double, radius: Int = 100): RegeocodeAddress? {
        val s = GeocodeSearch(context)
        val q = RegeocodeQuery(LatLonPoint(lat, lng), radius.toFloat(), GeocodeSearch.AMAP)
        return s.getFromLocation(q)
    }

    fun addressAsync(context: Context, lat: Double, lng: Double, radius: Int = 100, block: (RegeocodeAddress) -> Unit) {
        val s = GeocodeSearch(context)
        s.setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
            override fun onGeocodeSearched(r: GeocodeResult?, code: Int) {
            }

            override fun onRegeocodeSearched(r: RegeocodeResult?, code: Int) {
                if (code == 1000) {
                    if (r != null && r.regeocodeAddress != null) {
                        block(r.regeocodeAddress)
                    } else {
                        Log.e("GaoDeMap", "地址转换错误 返回null ")
                    }
                } else {
                    Log.e("GaoDeMap", "地址转换错误: $code")
                }
            }

        })
        val q = RegeocodeQuery(LatLonPoint(lat, lng), radius.toFloat(), GeocodeSearch.AMAP)
        s.getFromLocationAsyn(q)
    }
}