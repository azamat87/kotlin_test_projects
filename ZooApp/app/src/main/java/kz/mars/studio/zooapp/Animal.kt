package kz.mars.studio.zooapp

/**
 * Created by Asus on 28.01.2018.
 */
class Animal {

    var name: String? = null
    var des: String? = null
    var image: Int? = null
    var isKiller: Boolean ?= null

    constructor(name: String, des: String, image: Int, isKiller: Boolean){
        this.name = name
        this.des = des
        this.image = image
        this.isKiller = isKiller
    }
}