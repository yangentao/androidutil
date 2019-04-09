package dev.entao.gaode

import com.amap.api.location.AMapLocation
import com.amap.api.services.geocoder.RegeocodeAddress

/**
 * Created by entaoyang@163.com on 2018-04-12.
 */

class Loc {
	var country: String = ""
	var province: String = ""
	var city: String = ""
	var cityCode: String = ""
	var district: String = ""
	var street: String = ""
	var streetNum: String = ""
	var address: String = ""
	var lat: Double = 0.0
	var lng: Double = 0.0
	var poiName: String = ""


	companion object {

		fun from(r: RegeocodeAddress, lat: Double, lng: Double, poiName: String): Loc {
			val l = Loc()
			l.country = r.country
			l.province = r.province
			l.city = r.city
			l.cityCode = r.cityCode
			l.district = r.district
			l.street = r.streetNumber.street
			l.streetNum = r.streetNumber.number
			l.address = r.formatAddress
			l.lat = lat
			l.lng = lng
			l.poiName = poiName
			return l
		}

		fun from(a: AMapLocation): Loc {
			val l = Loc()
			l.city = a.city
			l.cityCode = a.cityCode
			l.country = a.country
			l.province = a.province
			l.district = a.district
			l.street = a.street
			l.address = a.address
			l.poiName = a.poiName
			l.streetNum = a.streetNum
			l.lat = a.latitude
			l.lng = a.longitude
			return l
		}
	}
}