package dev.entao.gaode

import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.PoiItem
import com.amap.api.services.geocoder.RegeocodeAddress

/**
 * Created by entaoyang@163.com on 2018-04-12.
 */

class MapSelResult(val regeo: RegeocodeAddress, val poi: PoiItem?, val loc: LatLng) {


}