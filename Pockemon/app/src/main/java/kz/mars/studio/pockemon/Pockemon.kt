package kz.mars.studio.pockemon

import android.location.Location

/**
 * Created by Asus on 27.01.2018.
 */
class Pockemon {
    var name: String ?= null
    var des: String ?= null
    var image: Int ?= null
    var power: Double? = null
    var lat: Double? = null
    var lon: Double? = null
    var location: Location? = null
    var isCatch: Boolean = false
    constructor(image: Int, name: String, des: String, power: Double, lat: Double, lon: Double){
        this.name = name
        this.des = des
        this.image = image
        this.power = power
        this.location = Location(name)
        this.location!!.latitude = lat
        this.location!!.longitude = lon
        this.isCatch = false
    }
}